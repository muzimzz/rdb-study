package com.study.rdb_study.controller;

import com.study.rdb_study.dto.CustomerRequest;
import com.study.rdb_study.dto.CustomerResponse;
import com.study.rdb_study.dto.PasswordRequest;
import com.study.rdb_study.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public void save(@RequestBody CustomerRequest request) {
        customerService.save(request);
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody CustomerRequest request) {
        customerService.update(id, request);
    }

    @PutMapping("/changePassword/{id}")
    public void updatePassword(@PathVariable Long id, @RequestBody PasswordRequest request) {
        customerService.updatePassword(id, request.getInputPassword(), request.getNewPassword());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerService.deleteById(id);
    }
}
