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
    public void save(final Review review) { em.persist(review); }

    /**
     * 리뷰 단건 조회
     */
    public Review findOne(final Long reviewId) {
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
     * 리뷰 목록 오름차순 조회
     */
    public List<Review> findAllbyCreateAtASC() {
        return em.createQuery("select r from Review r order by r.createAt asc", Review.class)
                .getResultList();
    }

    /**
     * 리뷰 목록 내림차순 조회
     */
    public List<Review> findAllbyCreateAtDESC() {
        return em.createQuery("select r from Review r order by r.createAt desc", Review.class)
                .getResultList();
    }


    /**
     * 특정 작성자의 리뷰 조회
     */
    public List<Review> findByMemberId(final Long memberId) {

        return em.createQuery("select r from Review r join r.member m where m.id =:id", Review.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 병원의 리뷰 조회
     */
    public List<Review> findByTargetHospital(final String targetHospital) {

        return em.createQuery("select r from Review r where r.targetHospital = :targetHospital", Review.class)
                .setParameter("targetHospital", targetHospital)
                .getResultList();
    }

    public List<Review> findAllByMember() {

        return em.createQuery("select r from Review r join fetch r.member m", Review.class)
                .getResultList();
    }
}
