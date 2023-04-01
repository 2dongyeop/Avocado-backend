package io.wisoft.capstonedesign.domain.appointment.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * 특정 회원의 예약 정보 조회
     */
    @Query("select a from Appointment a join fetch a.member m where m.id = :id")
    public List<Appointment> findByMemberId(@Param("id") final Long id);


    /**
     * 특정 회원의 예약 정보 시간을 오름차순으로 정렬하여 조회
     */
    @Query("select a from Appointment a join fetch a.member m where m.id = :id order by a.createAt asc")
    public List<Appointment> findByMemberIdOrderByCreateAtAsc(@Param("id") final Long memberId);


    @Query("select a from Appointment a join fetch a.member m where m.id = :id order by a.createAt desc")
    public List<Appointment> findByMemberIdOrderByCreateAtDesc(@Param("id") final Long memberId);
}


