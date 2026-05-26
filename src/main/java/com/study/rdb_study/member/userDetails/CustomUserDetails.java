package com.study.rdb_study.member.userDetails;

import com.study.rdb_study.member.MemberRole;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long memberId;      // 추가된 핵심 필드
    private final String email;
    private final String password;
    private final MemberRole role;
    private final boolean enabled;

    public CustomUserDetails(Long memberId, String email, String password, MemberRole role, boolean enabled) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

// --------- 위로는 필수 Override, 아래는 선택 구현 (default메서드) ----------

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠금 (ex: 비밀번호 5회 틀릴 시)
        return UserDetails.super.isAccountNonLocked();  // true
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부 (ex: 90일마다 비밀번호 변경 기능)
        return UserDetails.super.isCredentialsNonExpired();  // true
    }
}
