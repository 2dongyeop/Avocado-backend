package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.HealthInfo;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
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
    public void save(final HealthInfo healthInfo) {
        em.persist(healthInfo);
    }

    /**
     * 건강정보 단건 조회
     */
    public HealthInfo findOne(final Long healthInfoId) {
        return em.find(HealthInfo.class, healthInfoId);
    }

    /**
     * 건강정보 목록 조회
     */
    public List<HealthInfo> findAll() {

        return em.createQuery("select hi from HealthInfo hi", HealthInfo.class)
                .getResultList();
    }

    /* 특정 병과의 건강정보 목록 조회 */
    public List<HealthInfo> findAllByDept(final HospitalDept dept) {

        return em.createQuery("select hi from HealthInfo hi where hi.dept =: dept", HealthInfo.class)
                .setParameter("dept", dept)
                .getResultList();
    }

    /* 건강정보 목록 오름차순 조회 */
    public List<HealthInfo> findAllOrderByCreateAsc() {

        return em.createQuery("select hi from HealthInfo hi order by hi.createAt", HealthInfo.class)
                .getResultList();
    }

    /* 건강정보 목록 내림차순 조회 */
    public List<HealthInfo> findAllOrderByCreateDesc() {

        return em.createQuery("select hi from HealthInfo hi order by hi.createAt desc", HealthInfo.class)
                .getResultList();
    }
}
