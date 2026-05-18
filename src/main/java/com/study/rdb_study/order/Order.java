package com.study.rdb_study.order;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Long memberId;
    private LocalDateTime orderDate;
    private String status;
    // JPA: Entity에 List(연관관계) 넣기, JDBC: Response에만 넣기
    // private List<OrderItem> orderItems;

    @Builder
    public Order(Long orderId, Long memberId, LocalDateTime orderDate, String status) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.status = status;
    }
}
