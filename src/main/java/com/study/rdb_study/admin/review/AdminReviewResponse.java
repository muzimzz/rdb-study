package com.study.rdb_study.admin.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AdminReviewResponse {

    private Long reviewId;
    private String memberName;
    private String productName;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private boolean isUpdated;
}
