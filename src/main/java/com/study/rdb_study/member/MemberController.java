package com.study.rdb_study.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    GET    /members/{id}         - 특정 회원 조회
    DELETE /members/{id}         - 물리 삭제 (극히 예외적)
*/

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findById(@PathVariable Long id) {
        MemberResponse response = memberService.findById(id);
        return ResponseEntity.ok(response);
    }

    // 관리자용
    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                       @RequestBody MemberRequest request) {
        memberService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        memberService.updatePassword(id, request.getInputPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long id) {
        memberService.withdraw(id);
        return ResponseEntity.noContent().build();
    }
}
