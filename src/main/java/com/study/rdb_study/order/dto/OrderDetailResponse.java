package com.study.rdb_study.order.dto;

import com.study.rdb_study.order.Order;
import com.study.rdb_study.orderItem.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDetailResponse {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemResponse> orderItems;
    private int totalPrice;

    @Builder
    public OrderDetailResponse(Long orderId, Long customerId, LocalDateTime orderDate, String status, List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.status = status;
        this.orderItems = orderItems;
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItemResponse::getTotalPrice)
                .sum();
    }

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