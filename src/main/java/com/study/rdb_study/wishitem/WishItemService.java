package com.study.rdb_study.wishitem;

import com.study.rdb_study.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WishItemService {
    private final WishItemRepository wishItemRepository;

    public void save(Long productId, Long memberId) {
        wishItemRepository.save(productId, memberId);
    }

    @Transactional(readOnly = true)
    public List<WishItemResponse> findAll(Long memberId) {
        return wishItemRepository.findAll(memberId);
    }

    public void delete(Long productId, Long memberId) {
        if (!wishItemRepository.existsByProductIdAndMemberId(productId, memberId)) {
            throw new NotFoundException("찜한 상품이 아닙니다");
        }
        wishItemRepository.delete(productId, memberId);
    }
}
