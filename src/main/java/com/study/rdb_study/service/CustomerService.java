package com.study.rdb_study.service;

import com.study.rdb_study.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.study.rdb_study.repository.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public void update(Customer customer) {
        customerRepository.update(customer);
    }

    public void updatePassword(Long id, String inputPassword, String newPassword) {
        Customer customer = findById(id);
        if (customer == null)
            throw new IllegalArgumentException("존재하지 않는 사용자");
        if (customer.getPassword().equals(inputPassword)) {
            customerRepository.updatePassword(customer.getCustomerId(), newPassword);
        }

        throw new IllegalArgumentException("잘못된 비밀번호");

    }

    public void deleteById(Long id) {
        if (!customerRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 사용자");
        else customerRepository.deleteById(id);
    }


}

