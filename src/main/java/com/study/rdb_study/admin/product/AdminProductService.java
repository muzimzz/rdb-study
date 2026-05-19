package com.study.rdb_study.admin.product;

import com.study.rdb_study.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductService {

    private final AdminProductRepository adminProductRepository;
    private final ProductRepository productRepository;

    public AdminProductResponse save(AdminProductRequest request) {
        return AdminProductResponse.toDto(productRepository.save(request.toEntity()));
    }

    public void update(Long id, AdminProductRequest request) {
        if (!productRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 상품");
        productRepository.update(request.toEntityWithId(id));
    }

    public void updateStatus(Long id, String status) {

        if (!productRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 상품");

        productRepository.updateStatus(id, status);
    }
}
