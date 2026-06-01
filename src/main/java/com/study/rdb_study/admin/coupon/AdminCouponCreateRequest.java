package com.study.rdb_study.admin.coupon;

import com.study.rdb_study.coupon.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminCouponCreateRequest {

    private String name;
    private String code;
    private int discountRate;
    private int minOrderAmount;
    private Integer maxDiscountAmount;
    private Integer maxIssueCount;
    private LocalDateTime expiredAt;

    public Coupon toEntity() {
        return Coupon.builder()
                .name(name)
                .code(code)
                .discountRate(discountRate)
                .minOrderAmount(minOrderAmount)
                .maxDiscountAmount(maxDiscountAmount)
                .maxIssueCount(maxIssueCount)
                .expiredAt(expiredAt)
                .build();
    }
}
