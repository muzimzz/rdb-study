package com.study.rdb_study;

import com.study.rdb_study.coupon.Coupon;
import com.study.rdb_study.coupon.CouponRepository;
import com.study.rdb_study.coupon.CouponService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class CouponConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 각 테스트 끝난 후 자동으로 DB 초기화
    @AfterEach
    void resetDb() {
        jdbcTemplate.update("DELETE FROM member_coupons WHERE member_id >= 4");
        jdbcTemplate.update("UPDATE coupons SET issued_count = 0 WHERE code = 'RACE_TEST'");
    }

    @Test
    void 테스트1_락없음_race_condition_발생() throws InterruptedException {
        long duration = runTest("RACE_TEST", (memberId, code) ->
                couponService.registerByCode(memberId, code));

        printResult("락 없음 (race condition)", duration, "RACE_TEST");
    }

    @Test
    void 테스트2_Atomic_UPDATE_정상처리() throws InterruptedException {
        long duration = runTest("RACE_TEST", (memberId, code) ->
                couponService.registerByCodeAtomic(memberId, code));

        printResult("Atomic UPDATE", duration, "RACE_TEST");
    }

    @Test
    void 테스트3_FOR_UPDATE_정상처리() throws InterruptedException {
        long duration = runTest("RACE_TEST", (memberId, code) ->
                couponService.registerByCodeWithLock(memberId, code));

        printResult("FOR UPDATE (비관적 락)", duration, "RACE_TEST");
    }

    // 공통 테스트 로직
    private long runTest(String couponCode, CouponRegisterFunction registerFn) throws InterruptedException {
        int threadCount = 100;
        long startMemberId = 4L;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            long memberId = startMemberId + i; // 4 ~ 103
            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    registerFn.apply(memberId, couponCode);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        doneLatch.await();
        long duration = System.currentTimeMillis() - startTime;

        executor.shutdown();
        return duration;
    }

    private void printResult(String label, long duration, String couponCode) {
        Coupon coupon = couponRepository.findByCode(couponCode).get();
        System.out.println("\n========== [" + label + "] ==========");
        System.out.println("수행 시간       : " + duration + " ms");
        System.out.println("DB issued_count : " + coupon.getIssuedCount());
        System.out.println("maxIssueCount   : " + coupon.getMaxIssueCount());
        if (coupon.getIssuedCount() > coupon.getMaxIssueCount()) {
            System.out.println("결과: race condition 발생! " + coupon.getIssuedCount() + "개 초과 발급됨");
        } else {
            System.out.println("결과: 정상 처리됨");
        }
        System.out.println("==========================================");
    }

    // 람다로 메서드 전달하기 위한 함수형 인터페이스
    @FunctionalInterface
    interface CouponRegisterFunction {
        void apply(Long memberId, String code) throws Exception;
    }
}