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

        return em.createQuery("select rr from ReviewReply rr join rr.review r where r.id = :id", ReviewReply.class)
                .setParameter("id", reviewId)
                .getResultList();
    }

    /**
     * 특정 리뷰의 댓글 목록 오름차순 조회
     */
    public List<ReviewReply> findAllcreateAtASC() {

        String jpql = "select rr from ReviewReply rr  order by rr.createAt asc";

        return em.createQuery(jpql, ReviewReply.class)
                .getResultList();
    }

    /**
     * 특정 리뷰의 댓글 목록 내림차순 조회
     */
    public List<ReviewReply> findAllcreateAtDESC() {

        String jpql = "select rr from ReviewReply rr  order by rr.createAt desc";

        return em.createQuery(jpql, ReviewReply.class)
                .getResultList();
    }
}
