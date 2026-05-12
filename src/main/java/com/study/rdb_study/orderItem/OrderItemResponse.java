package com.study.rdb_study.orderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemResponse {
    private String productName;
    private int price;
    private int quantity;
    private int totalPrice;

    @Builder
    public OrderItemResponse(String productName, int price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = price * quantity;
    }
}
