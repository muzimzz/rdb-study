package com.study.rdb_study.service;

import com.study.rdb_study.dto.OrderItemRequest;
import com.study.rdb_study.dto.OrderItemResponse;
import com.study.rdb_study.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public void save(OrderItemRequest request) {
        orderItemRepository.save(request.toEntity());
    }

    public void increaseQuantity(Long orderId, Long productId, int addQuantity) {
        orderItemRepository.increaseQuantity(orderId, productId, addQuantity);
    }

    public List<OrderItemResponse> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteByOrderId(Long orderId) {
        orderItemRepository.deleteByOrderId(orderId);
    }

    public void deleteByOrderIdAndProductId(Long orderId, Long productId) {
        orderItemRepository.deleteByOrderIdAndProductId(orderId, productId);
    }
}
