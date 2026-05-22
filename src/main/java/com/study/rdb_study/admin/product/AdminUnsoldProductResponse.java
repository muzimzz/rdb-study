package com.study.rdb_study.admin.product;

import com.study.rdb_study.product.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminUnsoldProductResponse {
    private Long productId;
    private String name;
    private ProductCategory category;
    private int price;
    private int stockQuantity;
    private LocalDateTime lastOrderDate;  // null이면 한 번도 안 팔린 상품
    private Long daysUnsold;              // 미판매 기간 (마지막 주문일 or 등록일 기준)
}
