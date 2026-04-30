package com.study.rdb_study.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItem> orderItems;

    @Builder
    public Order(Long orderId, Long customerId, LocalDateTime orderDate, String status, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.status = status;
        this.orderItems = orderItems;
    }
}
