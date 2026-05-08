package com.study.rdb_study.customer;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Customer {
    private Long customerId;
    private String name;
    private String email;
    private String password;
    private String address;
    private String status;
    private LocalDateTime joinDate;

    @Builder
    public Customer(Long customerId, String name, String email, String password, String address, String status, LocalDateTime joinDate) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.status = status;
        this.joinDate = joinDate;
    }
}
