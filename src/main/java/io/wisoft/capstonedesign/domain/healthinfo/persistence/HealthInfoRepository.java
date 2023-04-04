package io.wisoft.capstonedesign.domain.healthinfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {


    /* 특정 병과의 건강정보 목록 조회 */
    @Query("select hi from HealthInfo hi where hi.dept =:dept")
    List<HealthInfo> findAllByDept(@Param("dept") final HospitalDept dept);


    /** 건강정보 목록을 페이지별로 오름차순 조회하기 */
    @Query(value = "select hi from HealthInfo hi join fetch hi.staff s",
            countQuery = "select count(hi) from HealthInfo hi")
    Page<HealthInfo> findByUsingPagingOrderByCreateAtAsc(final Pageable pageable);


    /** 건강정보 목록을 페이지별로 내림차순 조회하기 */
    @Query(value = "select hi from HealthInfo hi join fetch hi.staff s",
            countQuery = "select count(hi) from HealthInfo hi")
    Page<HealthInfo> findByUsingPagingOrderByCreateAtDesc(final Pageable pageable);
}
