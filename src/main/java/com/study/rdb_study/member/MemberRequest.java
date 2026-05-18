package com.study.rdb_study.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

// memberCreateRequest, memberUpdateRequest로 분리 권장 고려?

@Getter
@NoArgsConstructor
public class MemberRequest {
    private String name;
    private String email;
    private String password;
    private String address;

    // save()용: password 포함
    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .address(this.address)
                .status("ACTIVE")
                .build();
    }

    // 로그인/세션 구현하면 id빼기
    // update()용: password 제외
    // 비밀번호 변경은 PasswordRequest
    public Member toEntity(Long id) {
        return Member.builder()
                .memberId(id)
                .name(this.name)
                .email(this.email)
                .address(this.address)
                .build();
    }
}
