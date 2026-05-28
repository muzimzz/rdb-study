package com.study.rdb_study.admin.coupon;

import com.study.rdb_study.coupon.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCouponCreateRequest {

    // TODO: 필요한 필드 추가
    // 예시) name, code, discountRate, minOrderAmount,
    //       maxDiscountAmount, maxIssueCount, expiredAt

    public Coupon toEntity() {
        // TODO
        return null;
    }
}
