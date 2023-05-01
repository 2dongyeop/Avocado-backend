package io.wisoft.capstonedesign.domain.review.application;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.review.persistence.ReviewRepository;
import io.wisoft.capstonedesign.domain.review.web.dto.CreateReviewRequest;
import io.wisoft.capstonedesign.domain.review.web.dto.UpdateReviewRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullReviewException;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    /**
     * 리뷰 작성
     */
    @Transactional
    public Long save(final CreateReviewRequest request) {

        validateStarPoint(request.starPoint());

        //엔티티 조회
        final Member member = memberService.findById(request.memberId());

        final Review review = Review.builder()
                .member(member)
                .title(request.title())
                .body(request.body())
                .starPoint(request.starPoint())
                .target_hospital(request.targetHospital())
//                .reviewPhotoPath("path1")
                .build();

        reviewRepository.save(review);
        return review.getId();
    }

    private void validateStarPoint(final int starPoint) {
        if (starPoint <= 0 || starPoint >= 6) {
            throw new IllegalValueException("별점의 범위는 1~5 사이여야 합니다.");
        }
    }


    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(final Long reviewId) {
        final Review review = findById(reviewId);
        review.delete();
    }

    /**
     * 리뷰 제목 및 본문 수정
     */
    @Transactional
    public void updateTitleBody(final Long reviewId, final UpdateReviewRequest request) {

        validateTitleBody(request);
        final Review review = findById(reviewId);

        review.updateTitleBody(request.newTitle(), request.newBody());
    }

    private void validateTitleBody(final UpdateReviewRequest request) {

        if (!StringUtils.hasText(request.newTitle()) || !StringUtils.hasText(request.newBody())) {
            throw new IllegalValueException("제목이나 본문이 비어있습니다.");
        }
    }


    /* 조회 로직 */
    public Review findById(final Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(NullReviewException::new);
    }

    public List<Review> findAll() { return reviewRepository.findAll(); }

    /** 상세 조회 */
    public Review findDetailById(final Long reviewId) {
        return reviewRepository.findDetailById(reviewId).orElseThrow(NullReviewException::new);
    }

    /** 리뷰 목록 페이징 조회 */
    public Page<Review> findByUsingPaging(final Pageable pageable) {
        return reviewRepository.findByUsingPaging(pageable);
    }

    /** 특정 병원의 리뷰 페이징 조회 */
    public Page<Review> findByTargetHospital(final String targetHospital, final Pageable pageable) {

        final Page<Review> page = reviewRepository.findByTargetHospitalUsingPaging(targetHospital, pageable);

        if (page.isEmpty()) {
            throw new IllegalValueException("해당 병원에 대한 리뷰는 존재하지 않습니다.");
        }
        return page;
    }
}
