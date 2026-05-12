package com.study.rdb_study.cartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponse {
    private Long cartItemId;
    private String productName;
    private int price;
    private int quantity;
    private int totalPrice;

    @Builder
    public CartItemResponse(Long cartItemId, String productName, int price, int quantity) {
        this.cartItemId = cartItemId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = price * quantity;
    }
}
