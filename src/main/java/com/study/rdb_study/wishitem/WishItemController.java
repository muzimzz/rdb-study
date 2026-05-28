package com.study.rdb_study.wishitem;

import com.study.rdb_study.member.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wish-items")
public class WishItemController {
    private final WishItemService wishItemService;

    // 찜 추가
    @PostMapping("/{productId}")
    public ResponseEntity<Void> save(@PathVariable Long productId,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        wishItemService.save(productId, userDetails.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 내 찜 목록
    @GetMapping("/me")
    public ResponseEntity<List<WishItemResponse>> findAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(wishItemService.findAll(userDetails.getMemberId()));
    }

    // 찜 취소
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Long productId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        wishItemService.delete(productId, userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
