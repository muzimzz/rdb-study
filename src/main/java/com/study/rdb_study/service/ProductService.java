package com.study.rdb_study.service;

import com.study.rdb_study.dto.ProductRequest;
import com.study.rdb_study.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.study.rdb_study.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void save(ProductRequest request) {
        productRepository.save(request.toEntity());
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.toDto(productRepository.findById(id));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::toDto)
                .collect(Collectors.toList());
        //      .toList(); Java16부터 사용가능, 불변리스트 반환

//        List<Product> products = productRepository.findAll();
//        List<ProductResponse> productResponses = new ArrayList<>();
//        for (Product product : products)
//            productResponses.add(ProductResponse.fromEntity(product));
//        return productResponses;
    }

    public void update(Long id, ProductRequest request) {
        productRepository.update(request.toEntityWithId(id));
    }

    public void deleteById(Long id) {

        // if (findById(id) == null)
        if (!productRepository.existsById(id))
            throw new IllegalArgumentException("존재하지 않는 상품");

        productRepository.deleteById(id);
    }
}
