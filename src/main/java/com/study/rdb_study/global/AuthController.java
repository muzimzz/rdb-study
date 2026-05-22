package com.study.rdb_study.global;

import com.study.rdb_study.global.exception.NotFoundException;
import com.study.rdb_study.member.Member;
import com.study.rdb_study.member.MemberRepository;
import com.study.rdb_study.member.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;

    // 로그인 후 현재 세션 사용자 정보 반환 (프론트에서 memberId 등 필요)
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        Member member = memberRepository.findByEmailPublic(email)
                .orElseThrow(() -> new NotFoundException("회원 조회 실패"));

        return ResponseEntity.ok(MemberResponse.toDto(member));
    }
}
