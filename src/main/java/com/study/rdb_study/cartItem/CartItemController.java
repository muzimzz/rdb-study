package com.study.rdb_study.cartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestBody CartItemRequest request) {
        cartItemService.addItem(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateQuantity(@PathVariable Long id,
                                               @RequestBody CartItemUpdateRequest request) {
        cartItemService.updateQuantity(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllItems(@RequestParam Long cartId) {
        cartItemService.deleteAllByCartId(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        cartItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
