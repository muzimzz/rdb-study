package com.study.rdb_study.service;

import com.study.rdb_study.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.study.rdb_study.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void save(Product product) {
        productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void update(Product product) {
        productRepository.update(product);
    }

    public void deleteById(Long id) {
        if (findById(id) == null)
            throw new IllegalArgumentException("존재하지 않는 상품");

        productRepository.deleteById(id);
    }
}
