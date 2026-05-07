package com.study.rdb_study.cart;

import com.study.rdb_study.cartItem.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private Long customerId;
    private LocalDateTime createdAt;
    private List<CartItemResponse> cartItems;

    public static CartResponse toDto (Cart cart, List<CartItemResponse> cartItems) {
        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomerId())
                .createdAt(cart.getCreatedAt())
                .cartItems(cartItems)
                .build();
    }
}
