package io.wisoft.capstonedesign.domain.businfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.BusArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusInfoRepository extends JpaRepository<BusInfo, Long> {

    /* 특정 지역의 버스 정보 조회 */
    @Query("select bi from BusInfo bi where bi.area =:area")
    public List<BusInfo> findByArea(@Param("area") final BusArea area);
}
