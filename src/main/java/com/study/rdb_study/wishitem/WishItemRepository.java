package com.study.rdb_study.wishitem;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WishItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<WishItemResponse> wishItemResponseRowMapper = (rs, rowNum) -> WishItemResponse.builder()
            .wishItemId(rs.getLong("wish_item_id"))
            .productId(rs.getLong("product_id"))
            .productName(rs.getString("product_name"))
            .price(rs.getInt("price"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .build();

    public void save(Long productId, Long memberId) {
        String sql = "insert into wish_items (product_id, member_id) values (?, ?)";
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, productId);
            pstmt.setLong(2, memberId);
            return pstmt;
        });
    }

    public List<WishItemResponse> findAll(Long memberId) {
        String sql = """
                select wi.wish_item_id,
                       p.product_id,
                       p.name as product_name,
                       p.price,
                       wi.created_at
                from wish_items wi
                    join products p on wi.product_id = p.product_id
                where wi.member_id = ?
                  and p.status = 'ON_SALE'
                order by wi.created_at desc
                """;

        return jdbcTemplate.query(sql, wishItemResponseRowMapper, memberId);
    }

    public boolean existsByProductIdAndMemberId(Long productId, Long memberId) {
        String sql = "select count(*) from wish_items where product_id = ? and member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId, memberId);
        return count != null && count > 0;
    }

    public void delete(Long productId, Long memberId) {
        String sql = "delete from wish_items where product_id = ? and member_id = ?";
        jdbcTemplate.update(sql, productId, memberId);
    }
}
