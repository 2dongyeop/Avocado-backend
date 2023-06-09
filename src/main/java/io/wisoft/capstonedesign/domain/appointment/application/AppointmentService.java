package io.wisoft.capstonedesign.domain.appointment.application;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.persistence.AppointmentRepository;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentRequest;
import io.wisoft.capstonedesign.domain.appointment.web.dto.UpdateAppointmentRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalDeptException;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;

@Slf4j
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

        validateAppointmentDate(request.appointmentDate());

        //엔티티 조회
        final Member member = memberService.findById(request.memberId());
        final Hospital hospital = hospitalService.findById(request.hospitalId());

        //예약 정보 생성
        final Appointment appointment = createAppointment(request, member, hospital);

        appointmentRepository.save(appointment);
        return appointment.getId();
    }

    /**
     * 예약 정보 삭제
     */
    @Transactional
    public void deleteAppointment(final Long appointmentId) {
        final Appointment appointment = findById(appointmentId);
        appointmentRepository.delete(appointment);
    }

    /**
     * 예약 정보 수정
     */
    @Transactional
    public void update(final Long appointmentId, final UpdateAppointmentRequest request) {

        try {
            validateDept(request.dept());

            final Appointment appointment = findById(appointmentId);
            appointment.update(HospitalDept.valueOf(request.dept()), request.comment(), request.appointName(), request.appointPhonenumber());
        } catch (IllegalDeptException e) {
            log.error("일치하는 dept가 존재하지 않습니다.");
            e.printStackTrace();
        }
    }

    private void validateAppointmentDate(final LocalDateTime appointmentDate) {
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new IllegalValueException("요청된 예약일자가 현재 날짜보다 이전 날짜입니다.", ErrorCode.ILLEGAL_DATE);
        }
    }

    private Appointment createAppointment(
            final CreateAppointmentRequest request, final Member member, final Hospital hospital) {

        return Appointment.builder()
                .member(member)
                .hospital(hospital)
                .dept(HospitalDept.valueOf(request.dept()))
                .comment(request.comment())
                .appointName(request.appointName())
                .appointPhonenumber(request.appointPhonenumber())
                .payStatus(PayStatus.NONE)
                .appointmentDate(request.appointmentDate())
                .build();
    }

    private void validateDept(final String dept) throws IllegalDeptException {
        final HospitalDept[] values = HospitalDept.values();
        final Iterator<HospitalDept> iterator = Arrays.stream(values).iterator();

        while (iterator.hasNext()) {
            HospitalDept hospitalDept = iterator.next();

            if (hospitalDept.toString().equals(dept)) {
                return;
            }
        }

        throw new IllegalDeptException("일치하는 dept가 존재하지 않습니다.", ErrorCode.ILLEGAL_HOSPITAL_DEPT);
    }


    /* 조회 로직 */

    public Appointment findDetailById(final Long appointmentId) {
        return appointmentRepository.findDetailById(appointmentId).orElseThrow(NotFoundException::new);
    }

    public Appointment findById(final Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(NotFoundException::new);
    }
}



