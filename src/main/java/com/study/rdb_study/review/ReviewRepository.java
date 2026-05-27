package com.study.rdb_study.review;

import com.study.rdb_study.admin.review.AdminReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Review> reviewRowMapper = (rs, rowNum) -> Review.builder()
            .reviewId(rs.getLong("review_id"))
            .memberId(rs.getLong("member_id"))
            .productId(rs.getLong("product_id"))
            .rating(rs.getInt("rating"))
            .content(rs.getString("content"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at") != null
                    ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
            .build();

    public Review save(Review review) {
        String sql = "insert into reviews (member_id, product_id, rating, content) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, review.getMemberId());
            pstmt.setLong(2, review.getProductId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getContent());

            return pstmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰"));
    }

    // 같은 고객의 같은 상품 중복 리뷰 작성 방지를 위한 검증 로직
    public boolean existsByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "select count(*) from reviews where member_id=? and product_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId);
        return count != null && count > 0;
    }

    // 리뷰 자세히 보기 (선택사항)
    public Optional<Review> findById(Long id) {
        String sql = "select * from reviews where review_id = ?";
        return jdbcTemplate.query(sql, reviewRowMapper, id)
                .stream()
                .findFirst();
    }

    // 내가 쓴 리뷰 목록
    public List<Review> findByMemberId(Long memberId) {
        String sql = "select * from reviews where member_id = ?";
        return jdbcTemplate.query(sql, reviewRowMapper, memberId);
    }

    // 상품 별 리뷰 목록
    public List<Review> findByProductId(Long productId) {
        String sql = "select * from reviews where product_id = ?";
        return jdbcTemplate.query(sql, reviewRowMapper, productId);
    }

    // 리뷰 수정
    public void updateById(Long id, int rating, String content) {
        String sql = "update reviews set rating = ?, content = ?, updated_at = now() where review_id = ?";
        jdbcTemplate.update(sql, rating, content, id);
    }

    public void deleteById(Long id) {
        String sql = "delete from reviews where review_id = ?";
        jdbcTemplate.update(sql, id);
    }

    // 관리자용 전체 조회
    public List<AdminReviewResponse> findAllWithDetails() {
        String sql = """
                select r.review_id,
                m.name as member_name,
                p.name as product_name,
                r.rating,
                r.content,
                r.created_at,
                r.updated_at
                from reviews r
                join members m on r.member_id = m.member_id
                join products p on r.product_id = p.product_id
                order by r.created_at desc
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> AdminReviewResponse.builder()
                        .reviewId(rs.getLong("review_id"))
                        .memberName(rs.getString("member_name"))
                        .productName(rs.getString("product_name"))
                        .rating(rs.getInt("rating"))
                        .content(rs.getString("content"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .isUpdated(rs.getTimestamp("updated_at") != null)
                        .build());
    }
}
