package com.study.rdb_study.dto;

import com.study.rdb_study.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;

    public static OrderResponse toDto(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }
}
