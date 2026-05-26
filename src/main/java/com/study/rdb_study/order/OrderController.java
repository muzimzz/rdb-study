package com.study.rdb_study.order;

import com.study.rdb_study.member.userDetails.CustomUserDetails;
import com.study.rdb_study.order.dto.OrderDetailResponse;
import com.study.rdb_study.order.dto.OrderCreateRequest;
import com.study.rdb_study.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDetailResponse> save(@RequestBody OrderCreateRequest orderCreateRequest,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        OrderDetailResponse response = orderService.save(userDetails.getMemberId(), orderCreateRequest);

        return ResponseEntity
                .created(URI.create("/orders/" + response.getOrderId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findByMemberId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<OrderResponse> response = orderService.findByMemberId(userDetails.getMemberId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> findById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(orderService.findById(id, userDetails.getMemberId()));
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
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        orderService.cancelOrder(id, userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

}
