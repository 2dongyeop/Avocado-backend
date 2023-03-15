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
    public Review findOne(Long reviewId) {
        return em.find(Review.class, reviewId);
    }

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

        return em.createQuery("select r from Review r join r.member m where m.id =:id", Review.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 병원의 리뷰 조회
     */
    public List<Review> findByTargetHospital(String targetHospital) {

        return em.createQuery("select r from Review r where r.target_hospital = :targetHospital", Review.class)
                .setParameter("targetHospital", targetHospital)
                .getResultList();
    }
}
