package io.wisoft.capstonedesign.domain.appointment.persistence;

import io.wisoft.capstonedesign.domain.appointment.web.dto.AppointmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * 예약 단건 상세 조회
     */
    @Query("select a from Appointment a join fetch a.hospital h where a.id =:id")
    Optional<Appointment> findDetailById(@Param("id") final Long id);


    /**
     * 특정 회원의 예약 정보 조회
     */
    @Query("select a from Appointment a join fetch a.member m where m.id = :id")
    List<Appointment> findByMemberId(@Param("id") final Long id);


    /**
     * 특정 회원의 특정 페이지 예약 정보 조회
     */
    @Query(value = "select a" +
            " from Appointment a" +
            " join fetch a.member m" +
            " join fetch a.hospital h" +
            " where m.id = :id",
            countQuery = "select count(a) from Appointment a where a.member.id = :id")
    Page<Appointment> findByMemberIdUsingPaging(
            @Param("id") final Long memberId,
            final Pageable pageable);
}


