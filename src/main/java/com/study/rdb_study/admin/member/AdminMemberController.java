package com.study.rdb_study.admin.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    // 관리자용
    @GetMapping
    public ResponseEntity<List<AdminMemberResponse>> findAll() {
        return ResponseEntity.ok(adminMemberService.findAll());
    }
}
