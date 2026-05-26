package com.study.rdb_study.member.userDetails;

import com.study.rdb_study.member.Member;
import com.study.rdb_study.member.MemberRepository;
import com.study.rdb_study.member.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->  new UsernameNotFoundException("존재하지 않는 회원:" + email));

        // User: UserDetails의 구현체
//        return User.builder()
//                .username(email)
//                .password(member.getPassword())
//                .roles(member.getRole().name())
//                .disabled(member.getStatus() == MemberStatus.INACTIVE)
//                .build();

        return new CustomUserDetails(
                member.getMemberId(),
                member.getEmail(),
                member.getPassword(),
                member.getRole(),
                member.getStatus() == MemberStatus.ACTIVE);
    }
}
