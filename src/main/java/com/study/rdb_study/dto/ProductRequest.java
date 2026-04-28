package com.study.rdb_study.dto;

import com.study.rdb_study.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private int price;
    private int stockQuantity;
    private String description;

    public Product toEntity() {
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .description(this.description)
                .build();
    }

}
