package com.study.rdb_study.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Long memberId;
    private String name;
    private String email;
    private String address;
    private MemberRole role;
    private LocalDateTime joinDate;

    public static MemberResponse toDto(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .address(member.getAddress())
                .role(member.getRole())
                .joinDate(member.getJoinDate())
                .build();

    }

}
