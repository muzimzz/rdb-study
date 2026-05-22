package com.study.rdb_study.product;

import lombok.*;

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

    @Builder
    public Product(Long productId, String name, ProductCategory category, int price, int stockQuantity, String description, ProductStatus status) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.status = status;
    }
}
