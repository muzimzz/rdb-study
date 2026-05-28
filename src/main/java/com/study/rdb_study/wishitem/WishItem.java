package com.study.rdb_study.wishitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishItem {
    private Long wishItemId;
    private Long memberId;
    private Long productId;
    private LocalDateTime createdAt;
}
