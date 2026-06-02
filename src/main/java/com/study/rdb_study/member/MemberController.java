package com.study.rdb_study.member;

import com.study.rdb_study.member.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> join(@RequestBody MemberRequest request) {
        MemberResponse response = memberService.join(request);

        return ResponseEntity
                .created(URI.create("/members/" + response.getMemberId()))
                .body(response);
    }
 /*
    # 일반 사용자용
    GET    /members/me           - 내 정보 조회
    PUT    /members/me           - 내 정보 수정
    PATCH  /members/me/password  - 비밀번호 변경
    PATCH  /members/me/deactivate - 회원 탈퇴 (status 변경)

    # 관리자용
    GET    /members              - 전체 조회
    GET    /members/{id}        - 특정 회원 조회
    DELETE /members/{id}       - 물리 삭제 (극히 예외적)
*/

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberResponse response = memberService.findById(userDetails.getMemberId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                       @RequestBody MemberRequest request) {
        memberService.update(userDetails.getMemberId(), request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PasswordChangeRequest request) {
        memberService.updatePassword(userDetails.getMemberId(), request.getInputPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/withdraw")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.withdraw(userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
