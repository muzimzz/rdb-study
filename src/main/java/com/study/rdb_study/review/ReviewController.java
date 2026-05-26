package com.study.rdb_study.review;

import com.study.rdb_study.member.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> save(@RequestBody ReviewRequest request,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        ReviewResponse response = reviewService.save(userDetails.getMemberId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> findById(@PathVariable Long id) {
        ReviewResponse response = reviewService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponse>> findByMemberId(@PathVariable Long id,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ReviewResponse> responseList = reviewService.findByMemberId(userDetails.getMemberId());
        return ResponseEntity.ok(responseList);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> findByProductId(@RequestParam Long productId) {
        List<ReviewResponse> responseList = reviewService.findByProductId(productId);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable Long id,
                                           @RequestBody ReviewRequest request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.updateById(id, userDetails.getMemberId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.deleteById(id, userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

}
