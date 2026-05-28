package com.study.rdb_study.coupon;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Coupon {

    private Long couponId;

    private String name;               // 쿠폰 이름 (ex. "신규 가입 10% 할인")

    /** 쿠폰 코드
     *  - NULL     : 관리자가 특정 회원에게 직접 발급하는 방식 (코드 없음)
     *  - 값 있음  : 사용자가 코드를 직접 입력해서 등록하는 방식 (UNIQUE)
     */
    private String code;

    private int discountRate;          // 할인율 (1~100). ex) 10 → 10% 할인

    private int minOrderAmount;        // 쿠폰 적용 최소 주문 금액. 0이면 제한 없음

    /** 최대 할인 금액 상한선
     *  - NULL     : 제한 없음 (discountRate 그대로 적용)
     *  - 값 있음  : 이 금액을 초과해서 할인하지 않음
     *  ex) 30% 할인 + maxDiscountAmount=5000 → 30000원 주문 시 할인은 5000원으로 cap
     */
    private Integer maxDiscountAmount;

    /** 총 발급 가능 수량
     *  - NULL     : 무제한 발급
     *  - 값 있음  : 선착순 N개 한정 (issuedCount와 비교해서 초과 여부 판단)
     */
    private Integer maxIssueCount;

    private int issuedCount;           // 현재까지 실제로 발급된 수. 동시성 제어의 핵심 컬럼

    private LocalDateTime expiredAt;   // 쿠폰 만료일. 이 시각 이후에는 사용/등록 불가

    private LocalDateTime createdAt;   // 쿠폰 생성일

    @Builder
    public Coupon(Long couponId, String name, String code, int discountRate,
                  int minOrderAmount, Integer maxDiscountAmount, Integer maxIssueCount,
                  int issuedCount, LocalDateTime expiredAt, LocalDateTime createdAt) {
        this.couponId = couponId;
        this.name = name;
        this.code = code;
        this.discountRate = discountRate;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.maxIssueCount = maxIssueCount;
        this.issuedCount = issuedCount;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    // 할인 금액 계산
    public int calculateDiscountAmount(int orderAmount) {
        int discounted = (int) (orderAmount * (discountRate / 100.0));
        if (maxDiscountAmount != null) {
            discounted = Math.min(discounted, maxDiscountAmount);
        }
        return discounted;
    }
}
