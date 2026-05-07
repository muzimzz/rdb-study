package com.study.rdb_study.order;

import com.study.rdb_study.orderItem.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItem> orderItems;

    public static OrderResponse toDto(Order order, List<OrderItem> orderItems) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .orderItems(orderItems)
                .build();
    }
}
