package io.wisoft.capstonedesign.domain.review.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    List<Review> findAllOrderByCreateAtAsc();

    /**
     * 리뷰 목록 내림차순 조회
     */
    @Query("select r from Review r order by r.createAt desc")
    List<Review> findAllOrderByCreateAtDesc();


    /** 특정 페이지의 리뷰 목록 오름차순 조회 */
    @Query(value = "select r from Review r", countQuery = "select count(r) from Review r")
    Page<Review> findByUsingPagingOOrderByCreateAtAsc(final Pageable pageable);


    /** 특정 페이지의 리뷰 목록 오름차순 조회 */
    @Query(value = "select r from Review r", countQuery = "select count(r) from Review r")
    Page<Review> findByUsingPagingOOrderByCreateAtDesc(final Pageable pageable);


    /**
     * 특정 작성자의 리뷰 조회
     */
    @Query("select r from Review r join fetch r.member m where m.id =:id")
    List<Review> findByMemberId(@Param("id") final Long memberId);

    /**
     * 특정 병원의 리뷰 조회
     */
    @Query("select r from Review r where r.targetHospital = :targetHospital")
    List<Review> findByTargetHospital(@Param("targetHospital") final String targetHospital);

    @Query("select r from Review r join fetch r.member m")
    List<Review> findAllByMember();
}
