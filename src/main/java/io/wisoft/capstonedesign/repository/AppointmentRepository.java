package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.controller.AppointmentSearch;
import io.wisoft.capstonedesign.domain.Appointment;
import io.wisoft.capstonedesign.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AppointmentRepository {

    private final EntityManager em;

    /**
     * 예약 정보 저장
     */
    public void save(final Appointment appointment) {
        em.persist(appointment);
    }

    /**
     * 예약 단건 조회
     */
    public Appointment findOne(final Long appointmentId) {
        return em.find(Appointment.class, appointmentId);
    }

    /**
     * 특정 회원의 예약 정보 조회
     */
    public List<Appointment> findByMemberId(final Long memberId) {

        return em.createQuery("select a from Appointment a join fetch a.member m where m.id = :id", Appointment.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 회원의 예약 정보 시간을 오름차순으로 정렬하여 조회
     */
    public List<Appointment> findByMemberIdASC(final Long memberId) {

        return em.createQuery("select a from Appointment a join fetch a.member m where m.id = :id order by a.createAt ASC", Appointment.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 회원의 예약 정보 시간을 내림차순으로 정렬하여 조회
     */
    public List<Appointment> findByMemberIdDESC(final Long memberId) {

        return em.createQuery("select a from Appointment a join fetch a.member m where m.id = :id order by a.createAt DESC", Appointment.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 검색 조건에 동적으로 쿼리를 생성해서 예약 엔티티를 조회
     */
    public List<Appointment> findAllByCriteria(final AppointmentSearch appointmentSearch) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> o = cq.from(Appointment.class);
        Join<Appointment, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (appointmentSearch.getAppointmentStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    appointmentSearch.getAppointmentStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(appointmentSearch.getMemberNickName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" +
                    appointmentSearch.getMemberNickName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));

        TypedQuery<Appointment> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }
}


