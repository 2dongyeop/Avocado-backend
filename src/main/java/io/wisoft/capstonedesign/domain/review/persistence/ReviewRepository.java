package io.wisoft.capstonedesign.domain.review.persistence;

import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    /**
     * 특정 페이지의 리뷰 목록 조회
     */
    @Query(value =
            "select r" +
                    " from Review r" +
                    " join fetch r.member m" +
                    " where r.status = 'WRITE'", countQuery = "select count(r) from Review r")
    Page<Review> findByUsingPaging(final Pageable pageable);


    /**
     * 특정 병원의 리뷰 조회
     */
    @Query(value =
            "select r" +
                    " from Review r" +
                    " where r.targetHospital = :targetHospital" +
                    " and r.status = 'WRITE'", countQuery = "select count(r) from Review r")
    Page<Review> findByTargetHospitalUsingPaging(
            @Param("targetHospital") final String targetHospital, final Pageable pageable);


    /**
     * 특정 병과의 리뷰 조회
     */
    @Query(value =
            "select r" +
                    " from Review r" +
                    " where r.targetDept = :dept" +
                    " order by r.starPoint desc ", countQuery = "select count(r) from Review r")
    Page<Review> findByDeptUsingPaging(
            @Param("dept") final HospitalDept dept, final Pageable pageable);



    /**
     * 상세 조회
     */
    @Query("select r from Review r" +
            " join fetch r.member m" +
            " join r.reviewReplyList rr" +
            " where r.id = :id")
    Optional<Review> findDetailById(@Param("id") final Long id);
}
