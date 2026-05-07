package com.study.rdb_study.order;

import com.study.rdb_study.order.dto.OrderDetailResponse;
import com.study.rdb_study.order.dto.OrderRequest;
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
    public ResponseEntity<OrderResponse> save(@RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.save(orderRequest);

        return ResponseEntity
                .created(URI.create("/orders/" + response.getOrderId()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                       @RequestBody OrderRequest orderRequest) {
        orderService.update(id, orderRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
