package io.wisoft.capstonedesign.domain.hospital.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    /* 병원 이름으로 조회 */
    @Query("select h from Hospital h where h.name = :name")
    List<Hospital> findByHospitalName(@Param("name") final String name);

    List<Hospital> findByName(final String name);
}
