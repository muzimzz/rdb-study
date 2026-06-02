package com.study.rdb_study.admin.coupon;

import com.study.rdb_study.coupon.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AdminCouponResponse {

    private Long couponId;
    private String name;
    private String code;
    private int discountRate;
    private int minOrderAmount;
    private Integer maxDiscountAmount;
    private Integer maxIssueCount;
    private int issuedCount;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    public static AdminCouponResponse toDto(Coupon coupon) {
        return AdminCouponResponse.builder()
                .couponId(coupon.getCouponId())
                .name(coupon.getName())
                .code(coupon.getCode())
                .discountRate(coupon.getDiscountRate())
                .minOrderAmount(coupon.getMinOrderAmount())
                .maxDiscountAmount(coupon.getMaxDiscountAmount())
                .maxIssueCount(coupon.getMaxIssueCount())
                .issuedCount(coupon.getIssuedCount())
                .expiredAt(coupon.getExpiredAt())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}
