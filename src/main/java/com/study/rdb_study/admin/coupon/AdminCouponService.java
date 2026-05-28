package com.study.rdb_study.admin.coupon;

import com.study.rdb_study.coupon.CouponRepository;
import com.study.rdb_study.coupon.MemberCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    // 쿠폰 생성
    public AdminCouponResponse createCoupon(AdminCouponCreateRequest request) {
        // TODO
        return null;
    }

    // 전체 쿠폰 목록 조회
    @Transactional(readOnly = true)
    public List<AdminCouponResponse> findAll() {
        // TODO
        return List.of();
    }

    // 특정 회원에게 쿠폰 직접 발급
    public void issueToMember(Long couponId, Long memberId) {
        // TODO: 1. 쿠폰 존재 여부 확인
        //       2. 만료 여부 확인
        //       3. 수량 한도 초과 여부 확인
        //       4. 중복 발급 여부 확인
        //       5. member_coupons INSERT + issued_count +1
    }
}
