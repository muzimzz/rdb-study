package com.study.rdb_study.admin.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;

    @Transactional(readOnly = true)
    public List<AdminMemberResponse> findAll() {
        return adminMemberRepository.findAll();
    }
}
