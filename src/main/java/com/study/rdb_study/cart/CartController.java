package com.study.rdb_study.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> findByMemberId(@RequestParam Long memberId) {
        return ResponseEntity.ok(cartService.findByMemberId(memberId));
    }
}
