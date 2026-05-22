package com.study.rdb_study.admin.product;

import com.study.rdb_study.product.Product;
import com.study.rdb_study.product.ProductCategory;
import com.study.rdb_study.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AdminProductResponse {

    private Long productId;
    private String name;
    private ProductCategory category;
    private int price;
    private int stockQuantity;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;

    public static AdminProductResponse toDto(Product product) {
        return AdminProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
