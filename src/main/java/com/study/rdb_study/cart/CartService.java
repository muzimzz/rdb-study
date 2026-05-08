package com.study.rdb_study.cart;

import com.study.rdb_study.cartItem.CartItemRepository;
import com.study.rdb_study.cartItem.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // security
    @Transactional(readOnly = true)
    public CartResponse findByCustomerId(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않음"));

        List<CartItemResponse> cartItems = cartItemRepository.findByCartId(cart.getCartId())
                .stream()
                .map(CartItemResponse::toDto)
                .collect(Collectors.toList());

        return CartResponse.toDto(cart, cartItems);
    }
}

