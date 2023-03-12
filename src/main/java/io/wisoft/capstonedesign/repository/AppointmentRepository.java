package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Appointment;
import io.wisoft.capstonedesign.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppointmentRepository {

    private final EntityManager em;

    /**
     * 예약 정보 저장
     */
    public void save(Appointment appointment) { em.persist(appointment); }

    /**
     * 예약 단건 조회
     */
    public Appointment findOne(Long appointmentId) {
        return em.find(Appointment.class, appointmentId);
    }

    /**
     * 특정 회원의 예약 정보 조회
     */
    public List<Appointment> findByMemberId(Long memberId) {

        return em.createQuery("select a from Appointment a join a.member m where m.id = :id", Appointment.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 회원의 예약 정보 시간을 오름차순으로 정렬하여 조회
     */
    public List<Appointment> findByMemberIdASC(Long memberId) {

        return em.createQuery("select a from Appointment a join a.member m where m.id = :id order by a.appointedAt ASC", Appointment.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 회원의 예약 정보 시간을 내림차순으로 정렬하여 조회
     */
    public List<Appointment> findByMemberIdDESC(Long memberId) {

        return em.createQuery("select a from Appointment a join a.member m where m.id = :id order by a.appointedAt DESC", Appointment.class)
                .setParameter("id", memberId)
                .getResultList();
    }
}
