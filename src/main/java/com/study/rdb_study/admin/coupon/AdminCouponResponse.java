package com.study.rdb_study.admin.coupon;

import com.study.rdb_study.coupon.Coupon;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminCouponResponse {

    // TODO: 필요한 필드 추가
    // 예시) couponId, name, code, discountRate, minOrderAmount,
    //       maxDiscountAmount, maxIssueCount, issuedCount, expiredAt, createdAt

    public static AdminCouponResponse toDto(Coupon coupon) {
        // TODO
        return null;
    }
}
