package io.wisoft.capstonedesign.domain.appointment.application;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentRequest;
import io.wisoft.capstonedesign.domain.appointment.web.dto.UpdateAppointmentRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullAppointmentException;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static io.wisoft.capstonedesign.setting.data.HospitalTestData.getDefaultHospital;
import static io.wisoft.capstonedesign.setting.data.MemberTestData.getDefaultMember;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest extends ServiceTest {
    @Autowired EntityManager em;
    @Autowired AppointmentService appointmentService;

    @Test
    public void 예약_저장() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성

        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //임시 요청 생성
        final CreateAppointmentRequest request = getCreateAppointmentRequest(member, hospital);

        //when -- 동작
        final Long saveId = appointmentService.save(request);

        //then -- 검증
        final Appointment appointment = appointmentService.findById(saveId);
        Assertions.assertThat(appointment.getAppointName()).isEqualTo(request.appointName());
    }


    @Test
    public void 예약_취소() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //임시 요청 생성

        final Long saveId = appointmentService.save(getCreateAppointmentRequest(member, hospital));

        //when -- 동작
        appointmentService.deleteAppointment(saveId);

        //then -- 검증
        assertThrows(NullAppointmentException.class, () -> {
            appointmentService.findById(saveId);
        });
    }


    @Test
    public void 예약_취소_실패() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //임시 요청 생성

        appointmentService.save(getCreateAppointmentRequest(member, hospital));

        //when -- 동작
        //then -- 검증
        assertThrows(NullAppointmentException.class, () -> {
            appointmentService.deleteAppointment(100L);
        });
    }


    @Test
    public void 예약_중복_취소요청() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //임시 요청 생성

        final Long saveId = appointmentService.save(getCreateAppointmentRequest(member, hospital));

        //when -- 동작
        //then -- 검증

        assertThrows(NullAppointmentException.class, () -> {
            appointmentService.deleteAppointment(saveId);
            appointmentService.deleteAppointment(saveId);
        });
    }

    @Test
    public void 예약_단건_조회_실패() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //임시 요청 생성
        appointmentService.save(getCreateAppointmentRequest(member, hospital));

        //when -- 동작
        //then -- 검증
        assertThrows(NullAppointmentException.class, () -> {
            appointmentService.findById(100L);
        });
    }

    @Test
    public void 예약정보_수정() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //임시 요청 생성

        final Long saveId = appointmentService.save(getCreateAppointmentRequest(member, hospital));

        //when -- 동작
        final Appointment appointment = appointmentService.findById(saveId);
        appointmentService.update(appointment.getId(), getUpdateAppointmentRequest());

        //then -- 검증
        Assertions.assertThat(appointment.getComment()).isEqualTo("코를 높이고 싶어요");
        Assertions.assertThat(appointment.getUpdatedAt()).isNotNull();
    }

    @NotNull
    private UpdateAppointmentRequest getUpdateAppointmentRequest() {
        return new UpdateAppointmentRequest(
                "DENTAL",
                "코를 높이고 싶어요",
                "이동",
                "011");
    }


    private CreateAppointmentRequest getCreateAppointmentRequest(final Member member, final Hospital hospital) {
        return CreateAppointmentRequest.builder()
                .memberId(member.getId())
                .hospitalId(hospital.getId())
                .dept("DENTAL")
                .comment("comment")
                .appointName("name")
                .appointPhonenumber("phone")
                .appointmentDate(LocalDateTime.now().plusMonths(1))
                .build();
    }
}