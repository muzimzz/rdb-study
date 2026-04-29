package com.study.rdb_study.dto;

import com.study.rdb_study.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder // https://developer-anxi.tistory.com/30
public class ProductResponse {
    private Long productId;
    private String name;
    private int price;
    private int stockQuantity;
    private String description;

    public static ProductResponse toDto(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .build();
    }
}
