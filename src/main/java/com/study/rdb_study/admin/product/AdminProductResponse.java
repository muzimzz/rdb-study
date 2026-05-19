package com.study.rdb_study.admin.product;

import com.study.rdb_study.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminProductResponse {

    private Long productId;
    private String name;
    private int price;
    private int stockQuantity;
    private String description;
    private String status;

    public static AdminProductResponse toDto(Product product) {
        return AdminProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .status(product.getStatus())
                .build();
    }
}
