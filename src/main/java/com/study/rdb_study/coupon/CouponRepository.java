package com.study.rdb_study.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) -> Coupon.builder()
            .couponId(rs.getLong("coupon_id"))
            .name(rs.getString("name"))
            .code(rs.getString("code"))
            .discountRate(rs.getInt("discount_rate"))
            .minOrderAmount(rs.getInt("min_order_amount"))
            .maxDiscountAmount(rs.getObject("max_discount_amount") != null
                    ? rs.getInt("max_discount_amount") : null)
            .maxIssueCount(rs.getObject("max_issue_count") != null
                    ? rs.getInt("max_issue_count") : null)
            .issuedCount(rs.getInt("issued_count"))
            .expiredAt(rs.getTimestamp("expired_at").toLocalDateTime())
            .createdAt(rs.getTimestamp("created_at") != null
                    ? rs.getTimestamp("created_at").toLocalDateTime() : null)
            .build();

    // 쿠폰 생성 (관리자용)
    public Coupon save(Coupon coupon) {
        String sql = """
                insert into coupons (name, code, discount_rate, min_order_amount, max_discount_amount, max_issue_count, expired_at)
                values (?, ?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, coupon.getName());
            pstmt.setString(2, coupon.getCode());
            pstmt.setInt(3, coupon.getDiscountRate());
            pstmt.setInt(4, coupon.getMinOrderAmount());
            pstmt.setInt(5, coupon.getMaxDiscountAmount());
            pstmt.setInt(6, coupon.getMaxIssueCount());
            pstmt.setTime(7, coupon.getExpiredAt());

            return pstmt;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new IllegalArgumentException("쿠폰 없음"));
    }

    public Optional<Coupon> findById(Long couponId) {
        // TODO
        return Optional.empty();
    }

    // 코드 입력 방식 등록 시 사용
    public Optional<Coupon> findByCode(String code) {
        // TODO
        return Optional.empty();
    }

    // ── 트랜잭션 실습용 ──────────────────────────────────────────

    // [문제 버전] 수량 체크 없이 그냥 +1 (race condition 발생)
    public void increaseIssuedCount(Long couponId) {
        // TODO
    }

    // [해결 버전 A] DB Atomic UPDATE - issued_count < max_issue_count 조건부로 +1
    // 반환값: true = 성공, false = 선착순 마감
    public boolean increaseIssuedCountIfAvailable(Long couponId) {
        // TODO
        return false;
    }

    // [해결 버전 B] 비관적 락 - SELECT ... FOR UPDATE
    // 주의: 반드시 @Transactional 내에서 호출해야 락이 의미 있음
    public Optional<Coupon> findByCodeForUpdate(String code) {
        // TODO
        return Optional.empty();
    }
}
