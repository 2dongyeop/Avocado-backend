package io.wisoft.capstonedesign.domain.healthinfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {


    /** 특정 병과의 건강정보 목록 페이징 조회 */
    @Query(value = "select hi from HealthInfo hi" +
            " join fetch hi.staff s" +
            " where hi.dept =:dept",
            countQuery = "select count(hi) from HealthInfo hi")
    Page<HealthInfo> findAllByDeptUsingPaging(@Param("dept") final HospitalDept dept, final Pageable pageable);


    /** 건강정보 목록을 페이지별로 조회하기 */
    @Query(value = "select hi from HealthInfo hi join fetch hi.staff s",
            countQuery = "select count(hi) from HealthInfo hi")
    Page<HealthInfo> findByUsingPaging(final Pageable pageable);


    /**
     * 건강정보 상세 조회
     */
    @Query(value = "select hi from HealthInfo hi" +
            " join fetch hi.staff s" +
            " where hi.id = :id",
            countQuery = "select count(hi) from HealthInfo hi")
    Optional<HealthInfo> findDetailById(@Param("id") final Long healthInfoId);
}
