package com.study.rdb_study.cart;

import com.study.rdb_study.cartItem.CartItemRepository;
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

    public CartResponse save(CartRequest request) {
        //
        return CartResponse.toDto(cartRepository.save(request.toEntity()), List.of());
    }

    }

