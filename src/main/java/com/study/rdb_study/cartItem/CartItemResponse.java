package com.study.rdb_study.cartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;

    public static CartItemResponse toDto(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCartId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
