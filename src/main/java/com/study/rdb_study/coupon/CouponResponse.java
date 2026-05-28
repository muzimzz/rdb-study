package com.study.rdb_study.coupon;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CouponResponse {

    private Long memberCouponId;
    private String couponName;
    private int discountRate;
    private int minOrderAmount;
    private Integer maxDiscountAmount;
    private boolean isExpired;
    private LocalDateTime expiredAt;
    private boolean isUsed;
    private LocalDateTime issuedAt;

    public static CouponResponse toDto(MemberCoupon memberCoupon, Coupon coupon) {
        return CouponResponse.builder()
                .memberCouponId(memberCoupon.getMemberCouponId())
                .couponName(coupon.getName())
                .discountRate(coupon.getDiscountRate())
                .minOrderAmount(coupon.getMinOrderAmount())
                .maxDiscountAmount(coupon.getMaxDiscountAmount())
                .isExpired(coupon.getExpiredAt().isBefore(LocalDateTime.now()))
                .expiredAt(coupon.getExpiredAt())
                .isUsed(memberCoupon.isUsed())
                .issuedAt(memberCoupon.getIssuedAt())
                .build();
    }

}
