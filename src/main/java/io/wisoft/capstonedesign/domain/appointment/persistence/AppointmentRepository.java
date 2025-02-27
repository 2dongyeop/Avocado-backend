package io.wisoft.capstonedesign.domain.appointment.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * 예약 단건 상세 조회
     */
    @Query("select a from Appointment a join fetch a.hospital h where a.id =:id")
    Optional<Appointment> findDetailById(@Param("id") final Long id);
}


