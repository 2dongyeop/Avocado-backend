package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Review;
import io.wisoft.capstonedesign.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 작성
     */
    @Transactional
    public Long save(Review review) {
        reviewRepository.save(review);
        return review.getId();
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);
        review.delete();
    }

    /* 조회 로직 */
    public List<Review> findByMemberId(Long memberId) { return reviewRepository.findByMemberId(memberId); }

    public Review findOne(Long reviewId) { return reviewRepository.findOne(reviewId); }

    public List<Review> findAll() { return reviewRepository.findAll(); }
}
