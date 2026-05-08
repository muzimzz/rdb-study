package com.study.rdb_study.customer;

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
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    public CustomerResponse join(CustomerRequest customerRequest) {
        Customer customer = customerRepository.save(customerRequest.toEntity());
        cartRepository.save(Cart.builder()
                .customerId(customer.getCustomerId())
                .build());
        return CustomerResponse.toDto(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        return CustomerResponse.toDto(customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("고객 조회 실패")));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::toDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, CustomerRequest customerRequest) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 사용자");
        }

        customerRepository.update(customerRequest.toEntity(id));
    }

    public void updatePassword(Long id, String inputPassword, String newPassword) {
        String originPassword = customerRepository.findPasswordById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        if (!originPassword.equals(inputPassword))
            throw new IllegalArgumentException("잘못된 비밀번호");

        customerRepository.updatePassword(id, newPassword);
    }

    public void withdraw(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(" 존재하지 않는 사용자"));

        if (customer.getStatus().equals("INACTIVE")) {
            throw new IllegalArgumentException("이미 탈퇴한 사용자");
        }

        else customerRepository.updateStatus(id, "INACTIVE");
    }


}

