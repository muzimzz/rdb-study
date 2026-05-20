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
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String representativeProductName;
    private int totalPrice;

    public static OrderResponse toDto(Order order, List<OrderItemResponse> orderItems) {
        String firstName = orderItems.get(0).getProductName();
        int remainProduct = orderItems.size() - 1;
        String repName = remainProduct > 0 ?
                firstName + " 외 " + remainProduct + "건" : firstName;

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .representativeProductName(repName)
                .totalPrice(orderItems.stream()
                        .mapToInt(OrderItemResponse::getTotalPrice).sum())
                .build();
    }
}
