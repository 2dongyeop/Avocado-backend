package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.BusInfo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BusInfoRepository {

    private final EntityManager em;

    /**
     * 버스정보 등록
     */
    public void save(BusInfo busInfo) {
        em.persist(busInfo);
    }


    /**
     * 버스정보 단건 조회
     */
    public BusInfo findOne(Long busInfoId) {
        return em.find(BusInfo.class, busInfoId);
    }

    /**
     * 버스정보 목록 조회
     */
    public List<BusInfo> findAll() {

        return em.createQuery("select bi from BusInfo bi", BusInfo.class)
                .getResultList();
    }
}
