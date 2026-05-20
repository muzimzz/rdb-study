package com.study.rdb_study.admin.member;

import com.study.rdb_study.member.MemberRole;
import com.study.rdb_study.member.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminMemberResponse {
    private Long memberId;
    private String name;
    private String email;
    private String address;
    private MemberStatus status;
    private MemberRole role;
    private LocalDateTime joinDate;
    private int totalOrderCount;    // 총 주문 건수
    private int totalPrice;         // 총 구매 금액
    private LocalDateTime lastOrderDate;    // 마지막 주문일

    @Builder
    public AdminMemberResponse(Long memberId, String name, String email, String address, MemberStatus status, MemberRole role, LocalDateTime joinDate, int totalOrderCount, int totalPrice, LocalDateTime lastOrderDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.status = status;
        this.role = role;
        this.joinDate = joinDate;
        this.totalOrderCount = totalOrderCount;
        this.totalPrice = totalPrice;
        this.lastOrderDate = lastOrderDate;
    }
}


