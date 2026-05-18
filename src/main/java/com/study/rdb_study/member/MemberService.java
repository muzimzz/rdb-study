package com.study.rdb_study.member;

import com.study.rdb_study.cart.Cart;
import com.study.rdb_study.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    public MemberResponse join(MemberRequest memberRequest) {
        Member member = memberRepository.save(memberRequest.toEntity());
        cartRepository.save(Cart.builder()
                .memberId(member.getMemberId())
                .build());
        return MemberResponse.toDto(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        return MemberResponse.toDto(memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 조회 실패")));
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::toDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, MemberRequest memberRequest) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 사용자");
        }

        memberRepository.update(memberRequest.toEntity(id));
    }

    public void updatePassword(Long id, String inputPassword, String newPassword) {
        String originPassword = memberRepository.findPasswordById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        if (!originPassword.equals(inputPassword))
            throw new IllegalArgumentException("잘못된 비밀번호");

        memberRepository.updatePassword(id, newPassword);
    }

    public void withdraw(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        if (member.getStatus().equals("INACTIVE")) {
            throw new IllegalArgumentException("이미 탈퇴한 사용자");
        }

        cartRepository.deleteByMemberId(id);
        memberRepository.updateStatus(id, "INACTIVE");
    }


}

