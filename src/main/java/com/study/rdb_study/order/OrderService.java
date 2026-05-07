package com.study.rdb_study.order;

import com.study.rdb_study.order.dto.OrderDetailResponse;
import com.study.rdb_study.order.dto.OrderRequest;
import com.study.rdb_study.order.dto.OrderResponse;
import com.study.rdb_study.orderItem.OrderItem;
import com.study.rdb_study.orderItem.OrderItemRepository;
import com.study.rdb_study.orderItem.OrderItemRequest;
import com.study.rdb_study.orderItem.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderResponse save(OrderRequest orderRequest) {
        Order order = orderRepository.save(orderRequest.toEntity());
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            orderItemRepository.save(itemRequest.toEntity(order.getOrderId()));
        }

        return OrderResponse.toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문 조회 실패"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
        return OrderDetailResponse.toDto(order, orderItems.stream()
                .map(OrderItemResponse::toDto)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::toDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, OrderRequest orderRequest) {
        if (!orderRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 주문");

        orderRepository.update(orderRequest.toEntityWithId(id));
    }

    public void deleteById(Long id) {
        if (!orderRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 주문");
        orderRepository.deleteById(id);
    }
}
