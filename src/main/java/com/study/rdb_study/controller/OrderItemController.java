package com.study.rdb_study.controller;

import com.study.rdb_study.dto.OrderItemRequest;
import com.study.rdb_study.dto.OrderItemResponse;
import com.study.rdb_study.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public void save(@RequestBody OrderItemRequest request) {
        orderItemService.save(request);
    }

    @PatchMapping({"/{orderId}/{productId}"})
    public void increaseQuantity(@PathVariable Long orderId,
                                 @PathVariable Long productId,
                                 @RequestParam OrderItemRequest request) {
        orderItemService.increaseQuantity(
                orderId,
                productId,
                request.getQuantity()
        );
    }

    @GetMapping("/{orderId}")
    public List<OrderItemResponse> findByOrderId(@PathVariable Long orderId) {
        return orderItemService.findByOrderId(orderId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteByOrderId(@PathVariable Long orderId) {
        orderItemService.deleteByOrderId(orderId);
    }

    @DeleteMapping("/{orderId}/{productId}")
    public void deleteByOrderIdAndProductId(@PathVariable Long orderId,
                                @PathVariable Long productId) {
        orderItemService.deleteByOrderIdAndProductId(orderId, productId);
    }



}
