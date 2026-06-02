package com.study.rdb_study.coupon;

import com.study.rdb_study.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    // 내 쿠폰 목록 조회
    @Transactional(readOnly = true)
    public List<CouponResponse> findAllByMemberId(Long memberId) {
        return memberCouponRepository.findByMemberId(memberId);
    }

    // ── 트랜잭션 실습용 ──────────────────────────────────────────

    // [문제 버전] 락 없이 구현 → race condition 발생 확인용
    public void registerByCode(Long memberId, String code) {
        //  1. 코드로 쿠폰 조회
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new BadRequestException("잘못된 쿠폰 코드"));

        //  2. 만료 여부 확인
        if (coupon.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new BadRequestException("기한이 만료된 쿠폰");

        //  3. 수량 체크 (issued_count >= max_issue_count)  ← 여기서 race condition
        if (coupon.getIssuedCount() >= coupon.getMaxIssueCount())
            throw new BadRequestException("수량이 소진된 쿠폰");

        //  4. 중복 발급 확인
        if (memberCouponRepository.existsByMemberIdAndCouponId(memberId, coupon.getCouponId()))
            throw new BadRequestException("이미 발급한 쿠폰");

        //  5. member_coupons INSERT + issued_count +1
        memberCouponRepository.save(memberId, coupon.getCouponId());
        couponRepository.increaseIssuedCount(coupon.getCouponId());

    }

    // [해결 버전 A] DB Atomic UPDATE 방식
    public void registerByCodeAtomic(Long memberId, String code) {
        // TODO: 1. 코드로 쿠폰 조회
        //       2. 만료 여부 확인
        //       3. 중복 발급 확인
        //       4. increaseIssuedCountIfAvailable() → false면 선착순 마감 예외
        //       5. member_coupons INSERT
    }

    // [해결 버전 B] 비관적 락(FOR UPDATE) 방식
    public void registerByCodeWithLock(Long memberId, String code) {
        // TODO: 1. findByCodeForUpdate() → row 잠금
        //       2. 만료 여부 확인
        //       3. 수량 체크 (이제 최신값 보장됨)
        //       4. 중복 발급 확인
        //       5. member_coupons INSERT + issued_count +1
    }
}
