package com.study.rdb_study.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Long customerId;
    private Long productId;
    private int quantity;
    private LocalDateTime orderDate;
    private String status;

    @Builder
    public Order(Long orderId, Long customerId, Long productId, int quantity, LocalDateTime orderDate, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.status = status;
    }
}
