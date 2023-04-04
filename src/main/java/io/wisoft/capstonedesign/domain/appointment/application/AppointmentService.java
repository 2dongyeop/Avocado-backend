package io.wisoft.capstonedesign.domain.appointment.application;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.persistence.AppointmentRepository;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentRequest;
import io.wisoft.capstonedesign.domain.appointment.web.dto.UpdateAppointmentRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullAppointmentException;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
            final CreateAppointmentRequest request) {

        //엔티티 조회
        Member member = memberService.findOne(request.getMemberId());
        Hospital hospital = hospitalService.findById(request.getHospitalId());

        //예약 정보 생성
        Appointment appointment = Appointment.builder()
                .member(member)
                .hospital(hospital)
                .dept(HospitalDept.valueOf(request.getDept()))
                .comment(request.getComment())
                .appointName(request.getAppointName())
                .appointPhonenumber(request.getAppointPhonenumber())
                .build();

        appointmentRepository.save(appointment);
        return appointment.getId();
    }

    /**
     * 예약 정보 삭제
     */
    @Transactional
    public void cancelAppointment(final Long appointmentId) {
        Appointment appointment = findById(appointmentId);
        appointment.cancel();
    }

    /**
     * 예약 정보 수정
     */
    @Transactional
    public void update(final Long appointmentId, final UpdateAppointmentRequest request) {

        validateParameter(request);
        Appointment appointment = findById(appointmentId);

        appointment.update(HospitalDept.valueOf(request.getDept()), request.getComment(), request.getAppointName(), request.getAppointPhonenumber());
    }

    private void validateParameter(final UpdateAppointmentRequest request) {

        if (!StringUtils.hasText(request.getAppointName()) || !StringUtils.hasText(request.getDept())
                || !StringUtils.hasText(request.getComment()) || !StringUtils.hasText(request.getAppointPhonenumber())) {
            throw new IllegalValueException("파라미터가 비어있어 업데이트할 수 없습니다.");
        }
    }


    /* 조회 로직 */
    public List<Appointment> findByMemberId(final Long memberId) {
        return appointmentRepository.findByMemberId(memberId);
    }

    public Appointment findById(final Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(NullAppointmentException::new);
    }

    /** 특정 회원의 특정 페이지 예약 정보 조회 - 오름차순 */
    public List<Appointment> findByMemberIdUsingPagingOrderByCreateAtAsc(
            final Long memberId,
            final int pageNumber) {

        PageRequest request = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.ASC, "createAt"));

        return appointmentRepository.findByMemberIdUsingPagingOrderByCreateAtAsc(memberId, request)
                .getContent();
    }

    /** 특정 회원의 특정 페이지 예약 정보 조회 - 내림차순 */
    public List<Appointment> findByMemberIdUsingPagingOrderByCreateAtDesc(
            final Long memberId,
            final int pageNumber) {

        PageRequest request = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "createAt"));

        return appointmentRepository.findByMemberIdUsingPagingOrderByCreateAtDesc(memberId, request)
                .getContent();
    }
}
