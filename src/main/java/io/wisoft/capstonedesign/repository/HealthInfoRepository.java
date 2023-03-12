package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.HealthInfo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HealthInfoRepository {

    private final EntityManager em;

    /**
     * 건강정보 저장
     */
    public void save(HealthInfo healthInfo) {
        em.persist(healthInfo);
    }

    /**
     * 건강정보 단건 조회
     */
    public HealthInfo findOne(Long healthInfoId) {
        return em.find(HealthInfo.class, healthInfoId);
    }

    /**
     * 건강정보 목록 조회
     */
    public List<HealthInfo> findAll() {

        return em.createQuery("select hi from HealthInfo hi", HealthInfo.class)
                .getResultList();
    }
}
