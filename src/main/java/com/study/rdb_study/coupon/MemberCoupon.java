package com.study.rdb_study.coupon;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberCoupon {

    private Long memberCouponId;       // 발급 내역 고유 ID (orders.member_coupon_id가 이 값을 참조)

    private Long memberId;             // 쿠폰을 발급받은 회원

    private Long couponId;             // 어떤 쿠폰인지 (coupons 테이블 참조)

    /** 사용 여부
     *  - false : 미사용 (기본값)
     *  - true  : 주문에 적용 완료. 주문 취소 시 다시 false로 되돌려야 함
     */
    private boolean isUsed;

    private LocalDateTime usedAt;      // 쿠폰 사용 시각. isUsed=false면 NULL

    private LocalDateTime issuedAt;    // 쿠폰 발급 시각

    @Builder
    public MemberCoupon(Long memberCouponId, Long memberId, Long couponId,
                        boolean isUsed, LocalDateTime usedAt, LocalDateTime issuedAt) {
        this.memberCouponId = memberCouponId;
        this.memberId = memberId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
        this.issuedAt = issuedAt;
    }
}
