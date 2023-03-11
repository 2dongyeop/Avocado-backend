package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Review;
import io.wisoft.capstonedesign.domain.ReviewReply;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewReplyRepository {

    private final EntityManager em;

    /**
     * 댓글저장
     */
    public void save(ReviewReply reviewReply) {
        em.persist(reviewReply);
    }

    /**
     * 댓글 단건 조회
     */
    public ReviewReply findOne(Long reviewReplyId) {
        return em.find(ReviewReply.class, reviewReplyId);
    }

    /**
     * 특정 리뷰의 댓글 목록 조회
     */
    public List<ReviewReply> findByReviewId(Long reviewId) {

        Review targetReview = em.find(Review.class, reviewId);

        return em.createQuery("select rr from ReviewReply rr where rr.review = :targetReview", ReviewReply.class)
                .setParameter("targetReview", targetReview)
                .getResultList();
    }
}
