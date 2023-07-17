package io.wisoft.capstonedesign.domain.appointment.application;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.persistence.AppointmentRepository;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentRequest;
import io.wisoft.capstonedesign.domain.appointment.web.dto.UpdateAppointmentRequest;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTestV2 {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private HospitalService hospitalService;


    @Nested
    @DisplayName("예약 정보 작성")
    class CreateAppointment {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 예약에 성공한다.")
        public void 성공() throws Exception {

            //given
            final Member member = Member.newInstance(
                    "예약정보작성성공",
                    "예약정보작성성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Hospital hospital = Hospital.createHospital(
                    "예약정보작성성공병원",
                    "number",
                    "address",
                    "oper"
            );

            final var request = new CreateAppointmentRequest(
                    member.getId(),
                    hospital.getId(),
                    "DENTAL",
                    "comment",
                    "appointName",
                    "appointPhoneNumber",
                    LocalDateTime.now().plusMonths(1)
            );

            final Appointment appointment = Appointment.createAppointment(
                    member,
                    hospital,
                    HospitalDept.DENTAL,
                    request.comment(),
                    request.appointName(),
                    request.appointPhonenumber(),
                    PayStatus.NONE,
                    request.appointmentDate()
            );

            given(memberService.findById(any())).willReturn(member);
            given(hospitalService.findById(any())).willReturn(hospital);
            given(appointmentRepository.save(any())).willReturn(appointment);

            //when
            appointmentService.save(request);

            //then
            verify(appointmentRepository, times(1)).save(any());
        }
    }


    @Nested
    @DisplayName("예약 정보 삭제")
    class DeleteBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 예약이 삭제되어야 한다.")
        public void 성공() throws Exception {

            //given
            final Long appointmentId = 1L;

            final Member member = Member.newInstance(
                    "예약정보삭제성공",
                    "예약정보삭제성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Hospital hospital = Hospital.createHospital(
                    "예약정보삭제성공병원",
                    "number",
                    "address",
                    "oper"
            );

            final Appointment appointment = Appointment.createAppointment(
                    member,
                    hospital,
                    HospitalDept.DENTAL,
                    "comment",
                    "appointName",
                    "appointPhoneNumber",
                    PayStatus.NONE,
                    LocalDateTime.now().plusMonths(1)
            );

            given(appointmentRepository.findById(appointmentId)).willReturn(Optional.of(appointment));

            //when
            appointmentService.deleteAppointment(appointmentId);

            //then
            verify(appointmentRepository, times(1)).delete(any());
        }
    }


    @Nested
    @DisplayName("예약 정보 수정")
    class UpdateAppointment {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 예약 수정에 성공한다.")
        public void 성공() throws Exception {

            //given
            final Long appointmentId = 1L;

            final Member member = Member.newInstance(
                    "예약정보삭제성공",
                    "예약정보삭제성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Hospital hospital = Hospital.createHospital(
                    "예약정보삭제성공병원",
                    "number",
                    "address",
                    "oper"
            );

            final Appointment appointment = Appointment.createAppointment(
                    member,
                    hospital,
                    HospitalDept.DENTAL,
                    "comment",
                    "appointName",
                    "appointPhoneNumber",
                    PayStatus.NONE,
                    LocalDateTime.now().plusMonths(1)
            );

            final var request = new UpdateAppointmentRequest(
                    "DENTAL",
                    "comment2",
                    "appointName2",
                    "appointPhoneNumber2"
            );

            given(appointmentRepository.findById(appointmentId)).willReturn(Optional.of(appointment));

            //when
            appointmentService.update(appointmentId, request);

            //then
            Assertions.assertThat(appointment.getComment()).isEqualTo("comment2");
            Assertions.assertThat(appointment.getAppointName()).isEqualTo("appointName2");
            Assertions.assertThat(appointment.getAppointPhonenumber()).isEqualTo("appointPhoneNumber2");
        }
    }


    @Nested
    @DisplayName("예약 정보 조회")
    class ReadAppointment {

        @Test
        @DisplayName("예약 단건 조회 성공")
        public void 성공1() throws Exception {

            //given
            final Long appointmentId = 1L;

            final Member member = Member.newInstance(
                    "예약정보조회성공1",
                    "예약정보조회성공1@email.com",
                    "pass12",
                    "phoneNumber"
            );

            final Hospital hospital = Hospital.createHospital(
                    "예약정보조회성공1병원",
                    "number",
                    "address",
                    "oper"
            );

            final Appointment appointment = Appointment.createAppointment(
                    member,
                    hospital,
                    HospitalDept.DENTAL,
                    "comment",
                    "appointName",
                    "appointPhoneNumber",
                    PayStatus.NONE,
                    LocalDateTime.now().plusMonths(1)
            );

            given(appointmentRepository.findById(appointmentId)).willReturn(Optional.of(appointment));

            //when
            final Appointment result = appointmentService.findById(appointmentId);

            //then
            Assertions.assertThat(result.getAppointPhonenumber()).isEqualTo(appointment.getAppointPhonenumber());
            Assertions.assertThat(result.getAppointName()).isEqualTo(appointment.getAppointName());
            Assertions.assertThat(result.getComment()).isEqualTo(appointment.getComment());
        }
    }
}
