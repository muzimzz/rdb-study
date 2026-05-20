package com.study.rdb_study.admin.product;

import com.study.rdb_study.product.Product;
import com.study.rdb_study.product.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminProductRequest {
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
                .status(ProductStatus.ON_SALE)
                .build();
    }

    // JPA에서는 Entity안에 update메서드 작성 (Dirty Checking)
    public Product toEntityWithId(Long id) {
        return Product.builder()
                .productId(id)
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .description(this.description)
                .build();
    }

}
