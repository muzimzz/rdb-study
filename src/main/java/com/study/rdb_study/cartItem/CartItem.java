package com.study.rdb_study.cartItem;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItem {
    private Long cartItemId;
    private Long cartId;
    private Long productId;
    private int quantity;

    @Builder
    public CartItem(Long cartItemId, Long cartId, Long productId, int quantity) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
