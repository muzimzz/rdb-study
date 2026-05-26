package com.study.rdb_study.cartItem;

import com.study.rdb_study.cart.Cart;
import com.study.rdb_study.cart.CartRepository;
import com.study.rdb_study.global.exception.BadRequestException;
import com.study.rdb_study.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;


    // 장바구니에 상품 추가
    public void addItem(Long memberId, CartItemRequest request) {
        Cart cart = cartRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 장바구니"));

        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), request.getProductId());
        if (existing.isPresent()) {
            cartItemRepository.addQuantity(existing.get().getCartItemId(),  request.getQuantity());
        } else {
            cartItemRepository.save(request.toEntity(cart.getCartId()));
        }
    }

    // 장바구니 수량 변경
    public void updateQuantity(Long id, CartItemUpdateRequest request) {
        if (request.getQuantity() <= 0) {
            throw new BadRequestException("잘못된 수량 입력");
        }
        cartItemRepository.updateQuantity(id, request.toEntity());
    }

    // 장바구니 전체 삭제 (주문 완료 시 등)
    public void deleteAllByCartId(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }

    // 장바구니 상품 삭제
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
