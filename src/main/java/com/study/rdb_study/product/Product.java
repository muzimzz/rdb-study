package com.study.rdb_study.product;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Product {
    private Long productId;
    private String name;
    private ProductCategory category;
    private int price;
    private int stockQuantity;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;

    @Builder
    public Product(Long productId, String name, ProductCategory category, int price, int stockQuantity, String description, ProductStatus status, LocalDateTime createdAt) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }
}
