package com.study.rdb_study.review;

import com.study.rdb_study.global.exception.BadRequestException;
import com.study.rdb_study.global.exception.ForbiddenException;
import com.study.rdb_study.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewResponse save(Long memberId, ReviewRequest request) {
        return ReviewResponse.toDto(reviewRepository.save(request.toEntity(memberId)));
    }

    @Transactional(readOnly = true)
    public ReviewResponse findById(Long id) {
        return ReviewResponse.toDto(reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 리뷰")));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByMemberId(Long memberId) {
        return reviewRepository.findByMemberId(memberId).stream()
                .map(ReviewResponse::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(ReviewResponse::toDto)
                .collect(Collectors.toList());
    }

    public void updateById(Long id, Long memberId, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 리뷰"));

        if (!memberId.equals(review.getMemberId())) {
            throw new ForbiddenException("자신의 리뷰만 수정 가능");
        }

        if (review.getCreatedAt().isBefore(LocalDateTime.now().minusDays(3))) {
            throw new BadRequestException("작성 후 3일 지난 리뷰는 수정 불가");
        }

        reviewRepository.updateById(id, request.getRating(), request.getContent());
    }

    public void deleteById(Long id, Long memberId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 리뷰"));

        if (!memberId.equals(review.getMemberId())) {
            throw new ForbiddenException("자신의 리뷰만 삭제 가능");
        }

        reviewRepository.deleteById(id);
    }
}
