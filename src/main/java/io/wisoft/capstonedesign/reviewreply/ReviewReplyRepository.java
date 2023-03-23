package io.wisoft.capstonedesign.reviewreply;

import io.wisoft.capstonedesign.reviewreply.ReviewReply;
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
    public void save(final ReviewReply reviewReply) {
        em.persist(reviewReply);
    }

    /**
     * 댓글 단건 조회
     */
    public ReviewReply findOne(final Long reviewReplyId) {
        return em.find(ReviewReply.class, reviewReplyId);
    }

    /**
     * 특정 리뷰의 댓글 목록 조회
     */
    public List<ReviewReply> findByReviewId(final Long reviewId) {

        return em.createQuery("select rr from ReviewReply rr join fetch rr.review r where r.id = :id", ReviewReply.class)
                .setParameter("id", reviewId)
                .getResultList();
    }

    /**
     * 특정 리뷰의 댓글 목록 오름차순 조회
     */
    public List<ReviewReply> findAllOrderByCreateAtAsc(final Long id) {

        return em.createQuery("select rr from ReviewReply rr join fetch rr.review r where r.id = :id order by rr.createAt asc", ReviewReply.class)
                .setParameter("id", id)
                .getResultList();
    }

    /**
     * 특정 리뷰의 댓글 목록 내림차순 조회
     */
    public List<ReviewReply> findAllOrderByCreateAtDesc(final Long id) {

        String jpql = "select rr from ReviewReply rr  order by rr.createAt desc";

        return em.createQuery("select rr from ReviewReply rr join fetch rr.review r where r.id = :id order by rr.createAt desc", ReviewReply.class)
                .setParameter("id", id)
                .getResultList();
    }
}
