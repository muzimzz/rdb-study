package com.study.rdb_study.order.dto;

import com.study.rdb_study.order.Order;
import com.study.rdb_study.orderItem.OrderItem;
import com.study.rdb_study.orderItem.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemResponse> orderItems;

    public static OrderDetailResponse toDto(Order order, List<OrderItemResponse> orderItems) {
        return OrderDetailResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .orderItems(orderItems)
                .build();
    }
}