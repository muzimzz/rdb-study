package com.study.rdb_study.controller;

import com.study.rdb_study.dto.ProductRequest;
import com.study.rdb_study.dto.ProductResponse;
import com.study.rdb_study.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public void save(@RequestBody ProductRequest request) {
        productService.save(request);
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping
    public List<ProductResponse> findAll () {
        return productService.findAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody ProductRequest request) {
        productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        productService.deleteById(id);
    }

}
