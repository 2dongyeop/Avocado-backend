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

        //엔티티 조회
        Member member = memberService.findOne(request.getMemberId());

        Review review = Review.builder()
                .member(member)
                .title(request.getTitle())
                .body(request.getBody())
                .starPoint(request.getStarPoint())
                .target_hospital(request.getTargetHospital())
//                .reviewPhotoPath("path1")
                .build();

        reviewRepository.save(review);
        return review.getId();
    }


    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(final Long reviewId) {
        Review review = findOne(reviewId);
        review.delete();
    }

    /**
     * 리뷰 제목 및 본문 수정
     */
    @Transactional
    public void updateTitleBody(final Long reviewId, final UpdateReviewRequest request) {

        validateTitleBody(request);
        Review review = findOne(reviewId);

        review.updateTitleBody(request.getNewTitle(), request.getNewBody());
    }

    private void validateTitleBody(final UpdateReviewRequest request) {

        if (!StringUtils.hasText(request.getNewTitle()) || !StringUtils.hasText(request.getNewBody())) {
            throw new IllegalValueException("제목이나 본문이 비어있습니다.");
        }
    }


    /* 조회 로직 */
    public List<Review> findByMemberId(final Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    public Review findOne(final Long reviewId) {
        return reviewRepository.findOne(reviewId).orElseThrow(NullReviewException::new);
    }

    public List<Review> findAll() { return reviewRepository.findAll(); }

    public List<Review> findAllOrderByCreateAtASC() { return reviewRepository.findAllOrderByCreateAtASC(); }

    public List<Review> findAllOrderByCreateAtDESC() { return reviewRepository.findAllOrderByCreateAtDESC(); }

    public List<Review> findByTargetHospital(final String targetHospital) {

        List<Review> reviewListByTargetHospital = reviewRepository.findByTargetHospital(targetHospital);

        if (reviewListByTargetHospital.isEmpty()) {
            throw new IllegalValueException("해당 병원에 대한 리뷰는 존재하지 않습니다.");
        }
        return reviewListByTargetHospital;
    }

    public List<Review> findAllByMember() {
        return reviewRepository.findAllByMember();
    }
}
