package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.controller.AppointmentSearch;
import io.wisoft.capstonedesign.domain.Appointment;
import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.exception.IllegalValueException;
import io.wisoft.capstonedesign.exception.nullcheck.NullAppointmentException;
import io.wisoft.capstonedesign.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MemberService memberService;
    private final HospitalService hospitalService;

    /**
     * 예약 정보 작성
     */
    @Transactional
    public Long save(
            final Long memberId,
            final Long hospitalId,
            final HospitalDept dept,
            final String comment,
            final String appointName,
            final String appointPhonenumber) {

        //엔티티 조회
        Member member = memberService.findOne(memberId);
        Hospital hospital = hospitalService.findOne(hospitalId);

        //예약 정보 생성
        Appointment appointment = Appointment.createAppointment(member, hospital, dept, comment, appointName, appointPhonenumber);

        appointmentRepository.save(appointment);
        return appointment.getId();
    }

    /**
     * 예약 정보 삭제
     */
    @Transactional
    public void cancelAppointment(final Long appointmentId) {
        Appointment appointment = appointmentRepository.findOne(appointmentId);
        appointment.cancel();
    }

    /**
     * 예약 정보 수정
     */
    @Transactional
    public void update(
            final Long appointmentId,
            final HospitalDept dept,
            final String comment,
            final String appointName,
            final String appointPhonenumber) {

        Appointment appointment = findOne(appointmentId);
        validateParameter(dept, comment, appointName, appointPhonenumber);

        appointment.update(dept, comment, appointName, appointPhonenumber);
    }

    private void validateParameter(
            final HospitalDept dept,
            final String comment,
            final String appointName,
            final String appointPhonenumber) {

        if (dept == null || comment == null || appointName == null || appointPhonenumber == null) {
            throw new IllegalValueException("파라미터가 비어있어 업데이트할 수 없습니다.");
        }
    }


    /* 조회 로직 */
    public List<Appointment> findByMemberId(final Long memberId) { return appointmentRepository.findByMemberId(memberId); }

    public Appointment findOne(final Long appointmentId) {
        Appointment getAppointment = appointmentRepository.findOne(appointmentId);

        if (getAppointment == null) {
            throw new NullAppointmentException("해당 예약 정보가 존재하지 않습니다.");
        }

        return getAppointment;
    }

    public List<Appointment> findByMemberIdASC(final Long memberId) { return appointmentRepository.findByMemberIdASC(memberId); }

    public List<Appointment> findByMemberIdDESC(final Long memberId) { return appointmentRepository.findByMemberIdDESC(memberId); }

    public List<Appointment> findAllByCriteria(final AppointmentSearch appointmentSearch) {
        return appointmentRepository.findAllByCriteria(appointmentSearch);
    }
}
