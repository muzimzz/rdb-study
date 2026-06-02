package com.study.rdb_study.admin.coupon;

import com.study.rdb_study.coupon.Coupon;
import com.study.rdb_study.coupon.CouponRepository;
import com.study.rdb_study.coupon.MemberCouponRepository;
import com.study.rdb_study.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    // 쿠폰 생성
    public AdminCouponResponse createCoupon(AdminCouponCreateRequest request) {
        return AdminCouponResponse.toDto(couponRepository.save(request.toEntity()));
    }

    // 전체 쿠폰 목록 조회
    @Transactional(readOnly = true)
    public List<AdminCouponResponse> findAll() {
        return couponRepository.findAll()
                .stream()
                .map(AdminCouponResponse::toDto)
                .collect(Collectors.toList());
    }

    // 특정 회원에게 쿠폰 직접 발급
    public void issueToMember(Long couponId, Long memberId) {
        //  1. 쿠폰 존재 여부 확인
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 쿠폰"));

        //  2. 만료 여부 확인
        if (coupon.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new BadRequestException("기한이 만료된 쿠폰");

        //  3. 수량 한도 초과 여부 확인
        if (coupon.getMaxIssueCount() != null &&
                coupon.getIssuedCount() < coupon.getMaxIssueCount())
            throw new BadRequestException("발급 수량 초과");

        //  4. 중복 발급 여부 확인
        if (memberCouponRepository.existsByMemberIdAndCouponId(couponId, memberId))
            throw new BadRequestException("같은 회원에게 이미 발급된 쿠폰");

        //  5. member_coupons INSERT + issued_count +1
        memberCouponRepository.save(memberId, coupon.getCouponId());
        couponRepository.increaseIssuedCount(coupon.getCouponId());

    }
}
