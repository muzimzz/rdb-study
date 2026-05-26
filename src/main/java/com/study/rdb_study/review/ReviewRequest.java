package com.study.rdb_study.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequest {

    private int rating;
    private String content;
    private Long productId;

    public Review toEntity(Long memberId) {
        return Review.builder()
                .memberId(memberId)
                .productId(this.productId)
                .rating(this.rating)
                .content(this.content)
                .build();
    }

}
