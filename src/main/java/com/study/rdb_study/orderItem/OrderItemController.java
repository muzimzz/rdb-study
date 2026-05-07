package com.study.rdb_study.orderItem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItemResponse> save(@RequestBody OrderItemRequest request) {
        OrderItemResponse response = orderItemService.save(request);
        URI location = URI.create(String.format("/order-items/orders/%d/products/%d",
                response.getOrderId(),
                response.getProductId()));

        return ResponseEntity
                .created(location)
                .body(response);
    }

//    OrderItem대신 CartItem에 구현
//    @PatchMapping({"/{orderId}/{productId}"})
//    public ResponseEntity<OrderItemResponse> increaseQuantity(@PathVariable Long orderId,
//                                                          @PathVariable Long productId,
//                                                          @RequestParam int addQuantity) {
//        OrderItemResponse response = orderItemService.increaseQuantity(orderId, productId, addQuantity);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> findByOrderId(@PathVariable Long orderId) {
        List<OrderItemResponse> response = orderItemService.findByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteByOrderId(@PathVariable Long orderId) {
        orderItemService.deleteByOrderId(orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{orderId}/{productId}")
    public ResponseEntity<Void> deleteByOrderIdAndProductId(@PathVariable Long orderId,
                                @PathVariable Long productId) {
        orderItemService.deleteByOrderIdAndProductId(orderId, productId);
        return ResponseEntity.noContent().build();
    }
}
