package com.study.rdb_study.order;

import com.study.rdb_study.order.dto.OrderDetailResponse;
import com.study.rdb_study.order.dto.OrderCreateRequest;
import com.study.rdb_study.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDetailResponse> save(@RequestBody OrderCreateRequest orderCreateRequest) {
        OrderDetailResponse response = orderService.save(orderCreateRequest);

        return ResponseEntity
                .created(URI.create("/orders/" + response.getOrderId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findByCustomerId(@RequestParam Long customerId) {
        List<OrderResponse> response = orderService.findByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> findById(@PathVariable Long id, @RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.findById(id, customerId));
    }

    // 관리자용
//    @GetMapping
//    public ResponseEntity<List<OrderResponse>> findAll() {
//        return ResponseEntity.ok(orderService.findAll());
//    }

    // 관리자용
//    @PatchMapping("/{id}")
//    public ResponseEntity<Void> update(@PathVariable Long id,
//                       @RequestBody  orderRequest) {
//        orderService.update(id, orderRequest);
//
//        return ResponseEntity.noContent().build();
//    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

}
