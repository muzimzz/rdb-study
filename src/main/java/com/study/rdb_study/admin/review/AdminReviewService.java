package com.study.rdb_study.admin.review;

import com.study.rdb_study.global.exception.NotFoundException;
import com.study.rdb_study.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<AdminReviewResponse> findAll() {
        return reviewRepository.findAllWithDetails();
    }

    public void deleteReview(Long id) {
        reviewRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 리뷰"));

        reviewRepository.deleteById(id);
    }
}
