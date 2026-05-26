package com.study.rdb_study.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReviewResponse {

    /*
        ReviewResponse - boolean isUpdated Jackson 직렬화 주의
        boolean 타입에 is로 시작하는 필드명을 쓰면
        Jackson이 JSON으로 변환할 때 is를 제거해서
        "updated": true로 나와요.
        "isUpdated"로 내보내고 싶으면 Boolean(래퍼 타입)으로 바꾸거나
        @JsonProperty("isUpdated")를 붙여야 해요.
     */
    private Long reviewId;
    private Long memberId;
    private Long productId;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isUpdated;  // (수정됨 표시, updateAt이 null이면 false)

    public static ReviewResponse toDto(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .memberId(review.getMemberId())
                .productId(review.getProductId())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updateAt(review.getUpdateAt())
                .isUpdated(review.getUpdateAt() != null)
                .build();
    }
}
