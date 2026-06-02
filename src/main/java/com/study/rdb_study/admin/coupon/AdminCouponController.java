package com.study.rdb_study.admin.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final AdminCouponService adminCouponService;

    // 쿠폰 생성
    @PostMapping
    public ResponseEntity<AdminCouponResponse> createCoupon(
            @RequestBody AdminCouponCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminCouponService.createCoupon(request));
    }

    // 전체 쿠폰 목록 조회
    @GetMapping
    public ResponseEntity<List<AdminCouponResponse>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminCouponService.findAll());
    }

    // 특정 회원에게 쿠폰 직접 발급
    @PostMapping("/{couponId}/issue")
    public ResponseEntity<Void> issueToMember(
            @PathVariable Long couponId,
            @RequestBody AdminCouponIssueRequest request) {
        adminCouponService.issueToMember(couponId, request.getMemberId());
        return null;
    }
}
