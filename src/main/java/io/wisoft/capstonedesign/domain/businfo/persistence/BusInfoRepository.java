package io.wisoft.capstonedesign.domain.businfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.BusArea;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BusInfoRepository {

    private final EntityManager em;

    /**
     * 버스정보 등록
     */
    public void save(final BusInfo busInfo) {
        em.persist(busInfo);
    }


    /**
     * 버스정보 단건 조회
     */
    public Optional<BusInfo> findOne(final Long busInfoId) {
        return Optional.ofNullable(em.find(BusInfo.class, busInfoId));
    }

    /**
     * 버스정보 목록 조회
     */
    public List<BusInfo> findAll() {

        return em.createQuery("select bi from BusInfo bi order by bi.createAt", BusInfo.class)
                .getResultList();
    }

    /* 특정 지역의 버스 정보 조회 */
    public List<BusInfo> findByArea(final BusArea area) {

        return em.createQuery("select bi from BusInfo bi where bi.area =:area", BusInfo.class)
                .setParameter("area", area)
                .getResultList();
    }
}
