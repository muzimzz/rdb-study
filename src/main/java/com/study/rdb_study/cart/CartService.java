package com.study.rdb_study.cart;

import com.study.rdb_study.cartItem.CartItemRepository;
import com.study.rdb_study.cartItem.CartItemResponse;
import com.study.rdb_study.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // security
    @Transactional(readOnly = true)
    public CartResponse findByMemberId(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("장바구니가 존재하지 않음"));

        List<CartItemResponse> cartItems = cartItemRepository.findCartItemsByCartId(cart.getCartId());

        return CartResponse.toDto(cart, cartItems);
    }
}

