package com.study.rdb_study.dto;

import com.study.rdb_study.domain.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;

// CustomerCreateRequest, CustomerUpdateRequest로 분리 권장 고려?

@Getter
@NoArgsConstructor
public class CustomerRequest {
    private String name;
    private String email;
    private String password;
    private String address;

    // save()용: password 포함
    public Customer toEntity() {
        return Customer.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .address(this.address)
                .build();
    }

    // update()용: password 제외
    // 비밀번호 변경은 PasswordRequest
    public Customer toEntity(Long id) {
        return Customer.builder()
                .customerId(id)
                .name(this.name)
                .email(this.email)
                .address(this.address)
                .build();
    }
}
