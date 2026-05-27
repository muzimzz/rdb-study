package com.study.rdb_study.admin.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @GetMapping
    public ResponseEntity<List<AdminReviewResponse>> findAll() {
        return ResponseEntity.ok(adminReviewService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        adminReviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
