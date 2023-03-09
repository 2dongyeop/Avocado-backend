package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Hospital;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HospitalRepository {

    private final EntityManager em;

    /**
     * 병원 저장
     */
    public void save(Hospital hospital) { em.persist(hospital); }

    /**
     * 병원 단건 조회
     */
    public Hospital findOne(Long hospitalId) { return em.find(Hospital.class, hospitalId); }

    /**
     * 병원 목록 조회
     */
    public List<Hospital> findAll() {
        return em.createQuery("select h from Hospital h", Hospital.class)
                .getResultList();
    }
}
