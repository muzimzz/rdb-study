package com.study.rdb_study.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;

    public CustomerResponse save(CustomerRequest customerRequest) {
        return CustomerResponse.toDto(customerRepository.save(customerRequest.toEntity()));
    }

    public CustomerResponse findById(Long id) {
        return CustomerResponse.toDto(customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("고객 조회 실패")));
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerResponse::toDto)
                .collect(Collectors.toList());
    }

    public void update(Long id, CustomerRequest customerRequest) {
        customerRepository.update(customerRequest.toEntity(id));
    }

    public void updatePassword(Long id, String inputPassword, String newPassword) {
        String originPassword = customerRepository.findPasswordById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        if (!originPassword.equals(inputPassword))
            throw new IllegalArgumentException("잘못된 비밀번호");

        customerRepository.updatePassword(id, newPassword);
    }

    public void deleteById(Long id) {
        if (!customerRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 사용자");
        else customerRepository.deleteById(id);
    }


}

