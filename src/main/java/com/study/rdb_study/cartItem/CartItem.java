package com.study.rdb_study.cartItem;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItem {
    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;

    @Builder
    public CartItem(Long id, Long cartId, Long productId, int quantity) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
