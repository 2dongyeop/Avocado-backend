package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.domain.Review;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;

    /**
     * 리뷰 작성
     */
    public void save(Review review) { em.persist(review); }

    /**
     * 리뷰 단건 조회
     */
    public Review findOne(Long reviewId) { return em.find(Review.class, reviewId); }

    /**
     * 리뷰 전체 조회
     */
    public List<Review> findAll() {
        return em.createQuery("select r from Review r", Review.class)
                .getResultList();
    }

    /**
     * 특정 작성자의 리뷰 조회
     */
    public List<Review> findByMemberId(Long memberId) {
        Member targetMember = em.find(Member.class, memberId);

        return em.createQuery("select r from Review r where r.member =:targetMember", Review.class)
                .setParameter("targetMember", targetMember)
                .getResultList();
    }
}
