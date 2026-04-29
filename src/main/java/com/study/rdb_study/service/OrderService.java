package com.study.rdb_study.service;

import com.study.rdb_study.dto.OrderRequest;
import com.study.rdb_study.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.study.rdb_study.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void save(OrderRequest orderRequest) {
        orderRepository.save(orderRequest.toEntity());
    }

    public OrderResponse findById(Long id) {
        return OrderResponse.toDto(orderRepository.findById(id));
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::toDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, OrderRequest orderRequest) {
        orderRepository.update(orderRequest.toEntityWithId(id));
    }

    public void deleteById(Long id) {
        if (!orderRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 주문");
        orderRepository.deleteById(id);
    }
}
