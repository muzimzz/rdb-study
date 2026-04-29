package com.study.rdb_study.controller;

import com.study.rdb_study.dto.OrderRequest;
import com.study.rdb_study.dto.OrderResponse;
import com.study.rdb_study.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public void save(@RequestBody OrderRequest orderRequest) {
        orderService.save(orderRequest);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    public List<OrderResponse> findAll() {
        return orderService.findAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody OrderRequest orderRequest) {
        orderService.update(id, orderRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.deleteById(id);
    }
}
