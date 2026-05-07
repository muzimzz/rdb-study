package com.study.rdb_study.cartItem;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemRequest {
    private Long cartId;
    private Long productId;
    private int quantity;

    public CartItem toEntity() {
        return CartItem.builder()
                .cartId(this.cartId)
                .productId(this.productId)
                .quantity(this.quantity)
                .build();
    }
}
