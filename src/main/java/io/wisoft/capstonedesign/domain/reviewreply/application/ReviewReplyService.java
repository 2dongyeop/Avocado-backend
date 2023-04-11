package io.wisoft.capstonedesign.domain.reviewreply.application;

import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.review.application.ReviewService;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReplyRepository;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.CreateReviewReplyRequest;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.UpdateReviewReplyRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullReviewReplyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;
    private final MemberService memberService;
    private final ReviewService reviewService;

    /**
     * 리뷰댓글 저장
     */
    @Transactional
    public Long save(final CreateReviewReplyRequest request) {

        //엔티티 조회
        Member member = memberService.findById(request.getMemberId());
        Review review = reviewService.findById(request.getReviewId());

        ReviewReply reviewReply = ReviewReply.builder()
                .member(member)
                .review(review)
                .reply(request.getReply())
                .build();

        reviewReplyRepository.save(reviewReply);
        return reviewReply.getId();
    }


    /**
     * 리뷰댓글 삭제
     */
    @Transactional
    public void deleteReviewReply(final Long reviewReplyId) {

        ReviewReply reviewReply = findById(reviewReplyId);
        reviewReplyRepository.delete(reviewReply);
    }


    /**
     * 리뷰댓글 수정
     */
    @Transactional
    public void updateReply(final Long reviewReplyId, final UpdateReviewReplyRequest request) {

        validateParameter(request);
        ReviewReply reviewReply = findById(reviewReplyId);

        reviewReply.updateReply(request.getReply());
    }

    private void validateParameter(final UpdateReviewReplyRequest request) {

        if (!StringUtils.hasText(request.getReply())) {
            throw new IllegalValueException("reply가 비어있어 수정할 수 없습니다.");
        }
    }


    /*
     * 리뷰댓글 단건조회
     */
    public ReviewReply findById(final Long reviewReplyId) {
        return reviewReplyRepository.findById(reviewReplyId).orElseThrow(NullReviewReplyException::new);
    }

    /**
     * 리뷰댓글 단건 상세조회
     */
    public ReviewReply findDetailById(final Long reviewReplyId) {
        return reviewReplyRepository.findDetailById(reviewReplyId).orElseThrow(NullReviewReplyException::new);
    }


    /**
     * 특정 리뷰의 리뷰댓글 목록 조회
     */
    public List<ReviewReply> findByReviewId(final Long reviewId) {
        return reviewReplyRepository.findByReviewId(reviewId);
    }


    /**
     * 특정 리뷰의 댓글 목록 오름차순 조회
     */
    public List<ReviewReply> findAllByReviewIdOrderByCreateAsc(final Long reviewId) {
        return reviewReplyRepository.findAllOrderByCreatedAtAsc(reviewId);
    }


    /**
     * 특정 리뷰의 댓글 목록 내림차순 조회
     */
    public List<ReviewReply> findAllByReviewIdOrderByCreateDesc(final Long reviewId) {
        return reviewReplyRepository.findAllOrderByCreatedAtDesc(reviewId);
    }
}
