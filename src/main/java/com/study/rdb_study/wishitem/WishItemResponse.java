package com.study.rdb_study.wishitem;

import com.study.rdb_study.product.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class WishItemResponse {

    private Long wishItemId;
    private Long productId;
    private String productName;
    private int price;
    private LocalDateTime createdAt;
}
