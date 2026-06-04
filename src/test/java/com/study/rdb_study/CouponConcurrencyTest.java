package com.study.rdb_study;

import com.study.rdb_study.coupon.Coupon;
import com.study.rdb_study.coupon.CouponRepository;
import com.study.rdb_study.coupon.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void 동시에_100명이_선착순30개_쿠폰_요청시_초과발급_발생() throws InterruptedException {
        int threadCount = 100;
        long startMemberId = 4L;
        String couponCode = "RACE_TEST";

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount); // 스레드 준비 완료 신호
        CountDownLatch startLatch = new CountDownLatch(1);           // 동시 출발 신호
        CountDownLatch doneLatch = new CountDownLatch(threadCount);  // 전체 완료 대기

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            long memberId = startMemberId + i; // 4 ~ 103
            executor.submit(() -> {
                try {
                    readyLatch.countDown(); // 준비 완료 알림
                    startLatch.await();     // 모든 스레드 준비될 때까지 대기
                    // couponService.registerByCode(memberId, couponCode);
                    // couponService.registerByCodeAtomic(memberId, couponCode);
                    couponService.registerByCodeWithLock(memberId, couponCode);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("실패 원인: " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();     // 100개 스레드 모두 준비될 때까지 대기

        long startTime = System.currentTimeMillis();
        startLatch.countDown(); // 동시 출발!
        doneLatch.await();      // 모든 스레드 완료될 때까지 대기

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        executor.shutdown();

        Coupon coupon = couponRepository.findByCode(couponCode).get();

        System.out.println("========== 결과 ==========");
        System.out.println("락 병목 수행 시간 : " + duration + " ms");
        System.out.println("성공한 요청 수    : " + successCount.get());
        System.out.println("실패한 요청 수    : " + failCount.get());
        System.out.println("DB issued_count : " + coupon.getIssuedCount());
        System.out.println("maxIssueCount   : " + coupon.getMaxIssueCount());
        System.out.println("==========================");
        if (coupon.getIssuedCount() > coupon.getMaxIssueCount()) {
            System.out.println("race condition 발생! " + coupon.getIssuedCount() + "개 초과 발급됨");
        } else {
            System.out.println("정상 처리됨");
        }
    }
}