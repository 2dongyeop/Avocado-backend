package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Appointment;
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

    /**
     * 예약 정보 작성
     */
    @Transactional
    public Long save(Appointment appointment) {
        appointmentRepository.save(appointment);
        return appointment.getId();
    }

    /**
     * 예약 정보 삭제
     */
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findOne(appointmentId);
        appointment.cancel();
    }

    /* 조회 로직 */
    public List<Appointment> findByMemberId(Long memberId) { return appointmentRepository.findByMemberId(memberId); }

    public Appointment findOne(Long appointmentId) { return appointmentRepository.findOne(appointmentId); }

    public List<Appointment> findByMemberIdASC(Long memberId) { return appointmentRepository.findByMemberIdASC(memberId); }

    public List<Appointment> findByMemberIdDESC(Long memberId) { return appointmentRepository.findByMemberIdDESC(memberId); }
}
