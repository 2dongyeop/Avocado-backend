package io.wisoft.capstonedesign.domain.hospital.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HospitalRepository {

    private final EntityManager em;

    /**
     * 병원 저장
     */
    public void save(final Hospital hospital) {
        em.persist(hospital);
    }

    /**
     * 병원 단건 조회
     */
    public Optional<Hospital> findOne(final Long hospitalId) {
        return Optional.ofNullable(em.find(Hospital.class, hospitalId));
    }


    /* 병원 이름으로 조회 */
    public List<Hospital> findByHospitalName(final String name) {
        return em.createQuery("select h from Hospital h where h.name = :name", Hospital.class)
                .setParameter("name", name)
                .getResultList();
    }


    /**
     * 병원 목록 조회
     */
    public List<Hospital> findAll() {
        return em.createQuery("select h from Hospital h", Hospital.class)
                .getResultList();
    }
}
