package com.study.rdb_study.order;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Long memberId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Long memberCouponId;  // 사용한 쿠폰 발급 ID, 없으면 NULL
    private int discountAmount;   // 쿠폰 할인 금액, 미사용 시 0
    // JPA: Entity에 List(연관관계) 넣기, JDBC: Response에만 넣기
    // private List<OrderItem> orderItems;

    @Builder
    public Order(Long orderId, Long memberId, LocalDateTime orderDate, OrderStatus status,
                 Long memberCouponId, int discountAmount) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.status = status;
        this.memberCouponId = memberCouponId;
        this.discountAmount = discountAmount;
    }
}
