package com.study.rdb_study.coupon;

import com.study.rdb_study.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberCouponRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MemberCoupon> memberCouponRowMapper = (rs, rowNum) -> MemberCoupon.builder()
            .memberCouponId(rs.getLong("member_coupon_id"))
            .memberId(rs.getLong("member_id"))
            .couponId(rs.getLong("coupon_id"))
            .isUsed(rs.getBoolean("is_used"))
            .usedAt(rs.getTimestamp("used_at") != null
                    ? rs.getTimestamp("used_at").toLocalDateTime() : null)
            .issuedAt(rs.getTimestamp("issued_at") != null
                    ? rs.getTimestamp("issued_at").toLocalDateTime() : null)
            .build();

    private final RowMapper<CouponResponse> couponResponseRowMapper = (rs, rowNum) -> CouponResponse.builder()
            .memberCouponId(rs.getLong("member_coupon_id"))
            .couponName(rs.getString("name"))
            .discountRate(rs.getInt("discount_rate"))
            .minOrderAmount(rs.getInt("min_order_amount"))
            .maxDiscountAmount(rs.getObject("max_discount_amount") != null
                    ? rs.getInt("max_discount_amount") : null)
            .expiredAt(rs.getTimestamp("expired_at").toLocalDateTime())
            .isExpired(rs.getTimestamp("expired_at").toLocalDateTime()
                    .isBefore(LocalDateTime.now()))
            .isUsed(rs.getBoolean("is_used"))
            .issuedAt(rs.getTimestamp("issued_at").toLocalDateTime())
            .build();

    // 쿠폰 발급 (member_coupons INSERT)
    public MemberCoupon save(Long memberId, Long couponId) {
        String sql = "insert into member_coupons (member_id, coupon_id) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, memberId);
            pstmt.setLong(2, couponId);
            return pstmt;
        }, keyHolder);
        return findById(keyHolder.getKey().longValue())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 쿠폰"));

    }

    public Optional<MemberCoupon> findById(Long memberCouponId) {
        String sql = "select * from member_coupons where member_coupon_id = ?";

        return jdbcTemplate.query(sql, memberCouponRowMapper, memberCouponId)
                .stream().findFirst();
    }

    // 내 쿠폰 목록 (coupons JOIN해서 쿠폰 상세 정보 포함)
    public List<CouponResponse> findByMemberId(Long memberId) {
        String sql = """
                select mc.member_coupon_id,
                c.name,
                c.discount_rate,
                c.min_order_amount,
                c.max_discount_amount,
                c.expired_at,
                mc.is_used,
                mc.issued_at
                from member_coupons mc
                join coupons c on mc.coupon_id = c.coupon_id
                where mc.member_id = ?
                """;
        return jdbcTemplate.query(sql, couponResponseRowMapper, memberId);
    }

    // 중복 발급 방지 검증
    public boolean existsByMemberIdAndCouponId(Long memberId, Long couponId) {
        String sql = "select count(*) from member_coupons where member_id = ? and coupon_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, couponId);
        return count != null && count > 0;
    }

    // 쿠폰 사용 처리 (주문 시)
    public void markAsUsed(Long memberCouponId) {
        String sql = "update member_coupons set is_used = 1, used_at = now() where member_coupon_id = ?";
        jdbcTemplate.update(sql, memberCouponId);
    }

    // 쿠폰 반환 (주문 취소 시)
    public void markAsUnused(Long memberCouponId) {
        String sql = "update member_coupons set is_used = 0, used_at = null where member_coupon_id = ?";
        jdbcTemplate.update(sql, memberCouponId);
    }
}
