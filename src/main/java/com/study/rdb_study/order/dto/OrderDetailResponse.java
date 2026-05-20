package com.study.rdb_study.order.dto;

import com.study.rdb_study.order.Order;
import com.study.rdb_study.order.OrderStatus;
import com.study.rdb_study.orderItem.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDetailResponse {
    private Long orderId;
    private Long memberId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;
    private int totalPrice;

    @Builder
    public OrderDetailResponse(Long orderId, Long memberId, LocalDateTime orderDate, OrderStatus status, List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.memberId = memberId;
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
                .memberId(order.getMemberId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .orderItems(orderItems)
                .build();
    }
}