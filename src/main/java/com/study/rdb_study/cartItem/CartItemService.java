package com.study.rdb_study.cartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    // 장바구니 조회는 Cart에서 하는 것이 적절
    // public List<CartItemResponse> findByCartId(Long customerId) { }

    // 장바구니에 상품 추가
    public void addItem(CartItemRequest request) {
        cartItemRepository.save(request.toEntity());
    }

    // 장바구니 수량 변경
    public void updateQuantity(Long id, CartItemUpdateRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("잘못된 수량 입력");
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
