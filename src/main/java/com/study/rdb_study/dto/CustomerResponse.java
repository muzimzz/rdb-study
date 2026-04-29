package com.study.rdb_study.dto;

import com.study.rdb_study.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long customerId;
    private String name;
    private String email;
    private String address;
    private LocalDateTime joinDate;

    public static CustomerResponse toDto(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .joinDate(customer.getJoinDate())
                .build();

    }

}
