package io.wisoft.capstonedesign.domain.review.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    /**
     * 리뷰 목록 오름차순 조회
     */
    @Query("select r from Review r order by r.createAt asc")
    public List<Review> findAllOrderByCreateAtAsc();

    /**
     * 리뷰 목록 내림차순 조회
     */
    @Query("select r from Review r order by r.createAt desc")
    public List<Review> findAllOrderByCreateAtDesc();


    /**
     * 특정 작성자의 리뷰 조회
     */
    @Query("select r from Review r join fetch r.member m where m.id =:id")
    public List<Review> findByMemberId(@Param("id") final Long memberId);

    /**
     * 특정 병원의 리뷰 조회
     */
    @Query("select r from Review r where r.targetHospital = :targetHospital")
    public List<Review> findByTargetHospital(@Param("targetHospital") final String targetHospital);

    @Query("select r from Review r join fetch r.member m")
    public List<Review> findAllByMember();
}
