package com.study.rdb_study.member;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Member {
    private Long memberId;
    private String name;
    private String email;
    private String password;
    private String address;
    private String status;
    private LocalDateTime joinDate;

    @Builder
    public Member(Long memberId, String name, String email, String password, String address, String status, LocalDateTime joinDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.status = status;
        this.joinDate = joinDate;
    }
}
