package com.study.rdb_study.coupon;

import com.study.rdb_study.member.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 내 쿠폰 목록 조회
    @GetMapping("/me")
    public ResponseEntity<List<CouponResponse>> findMyCoupons(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(couponService.findAllByMemberId(userDetails.getMemberId()));
    }

    // 쿠폰 코드 입력으로 등록
    @PostMapping("/register")
    public ResponseEntity<Void> registerByCode(
            @RequestBody CouponRegisterRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        couponService.registerByCode(userDetails.getMemberId(), request.getCode());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
