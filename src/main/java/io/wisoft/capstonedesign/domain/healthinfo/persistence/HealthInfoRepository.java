package io.wisoft.capstonedesign.domain.healthinfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {


    /* 특정 병과의 건강정보 목록 조회 */
    @Query("select hi from HealthInfo hi where hi.dept =:dept")
    public List<HealthInfo> findAllByDept(@Param("dept") final HospitalDept dept);

    /* 건강정보 목록 오름차순 조회 */
    @Query("select hi from HealthInfo hi order by hi.createAt asc")
    public List<HealthInfo> findAllOrderByCreateAsc();

    /* 건강정보 목록 내림차순 조회 */
    @Query("select hi from HealthInfo hi order by hi.createAt desc")
    public List<HealthInfo> findAllOrderByCreateDesc();
}
