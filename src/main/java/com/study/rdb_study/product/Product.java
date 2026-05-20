package com.study.rdb_study.product;

import lombok.*;

@Getter
@NoArgsConstructor
public class Product {
    private Long productId;
    private String name;
    private int price;
    private int stockQuantity;
    private String description;
    private ProductStatus status;

    @Builder
    public Product(Long productId, String name, int price, int stockQuantity, String description, ProductStatus status) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.status = status;
    }
}
