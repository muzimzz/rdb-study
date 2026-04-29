package com.study.rdb_study.service;

import com.study.rdb_study.domain.Customer;
import com.study.rdb_study.dto.CustomerRequest;
import com.study.rdb_study.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.study.rdb_study.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;

    public void save(CustomerRequest customerRequest) {
        customerRepository.save(customerRequest.toEntity());
    }

    public CustomerResponse findById(Long id) {
        return CustomerResponse.toDto(customerRepository.findById(id));
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
        String originPassword = customerRepository.findPasswordById(id);
        // if (!customerRepository.existsById(id))
        if (originPassword == null)   // findByid() 쿼리 1번이면 충분. exist 추가쿼리 실행 필요x
            throw new IllegalArgumentException("존재하지 않는 사용자");

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

