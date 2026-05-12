package com.study.rdb_study.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse save(ProductRequest request) {
        return ProductResponse.toDto(productRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return ProductResponse.toDto(productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 조회 실패")));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::toDto)
                .collect(Collectors.toList());
        //      .toList(); Java16부터 사용가능, 불변리스트 반환
    }

    public void update(Long id, ProductRequest request) {
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
