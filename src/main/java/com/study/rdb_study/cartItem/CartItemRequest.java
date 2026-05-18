package com.study.rdb_study.cartItem;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemRequest {
    private Long memberId;
    private Long productId;
    private int quantity;

    public CartItem toEntity(Long cartId) {
        return CartItem.builder()
                .cartId(cartId)
                .productId(this.productId)
                .quantity(this.quantity)
                .build();
    }
}
