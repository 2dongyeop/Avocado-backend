package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentRequest;
import io.wisoft.capstonedesign.domain.appointment.web.dto.UpdateAppointmentRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.status.AppointmentStatus;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullAppointmentException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AppointmentServiceTest {

    @Autowired EntityManager em;
    @Autowired AppointmentService appointmentService;


    @Test
    public void 예약_저장() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //임시 요청 생성
        CreateAppointmentRequest request = new CreateAppointmentRequest(member.getId(), hospital.getId(), "DENTAL", "눈이 건조해요", "이동엽", "01012345678");

        //when -- 동작

        Long saveId = appointmentService.save(request);

        //then -- 검증
        Appointment appointment = appointmentService.findById(saveId);
        Assertions.assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.COMPLETE);
        Assertions.assertThat(appointment.getAppointName()).isEqualTo(request.getAppointName());
    }


    @Test
    public void 예약_취소() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //임시 요청 생성
        CreateAppointmentRequest request = new CreateAppointmentRequest(member.getId(), hospital.getId(), "DENTAL", "눈이 건조해요", "이동엽", "01012345678");
        Long saveId = appointmentService.save(request);

        //when -- 동작
        Appointment appointment = appointmentService.findById(saveId);
        appointment.cancel();

        //then -- 검증
        Assertions.assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCEL);
    }


    @Test(expected = IllegalStateException.class)
    public void 예약_중복_취소요청() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //임시 요청 생성
        CreateAppointmentRequest request = new CreateAppointmentRequest(member.getId(), hospital.getId(), "DENTAL", "눈이 건조해요", "이동엽", "01012345678");
        Long saveId = appointmentService.save(request);

        //when -- 동작
        appointmentService.cancelAppointment(saveId);
        appointmentService.cancelAppointment(saveId);

        //then -- 검증
        fail("중복 예약 취소 요청으로 인한 예외가 발생해야 한다.");
    }

    @Test(expected = NullAppointmentException.class)
    public void 예약_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Appointment appointment = appointmentService.findById(100L);

        //then -- 검증
        fail("해당 appointmentId에 일치하는 예약 정보가 없어 예외가 발생해야 한다.");
    }

    @Test
    public void 예약정보_수정() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //임시 요청 생성
        CreateAppointmentRequest request1 = new CreateAppointmentRequest(member.getId(), hospital.getId(), "DENTAL", "눈이 건조해요", "이동엽", "01012345678");
        Long saveId = appointmentService.save(request1);

        UpdateAppointmentRequest request2 = new UpdateAppointmentRequest("DENTAL", "코를 높이고 싶어요", "이동", "011");

        //when -- 동작
        Appointment appointment = appointmentService.findById(saveId);
        appointmentService.update(appointment.getId(), request2);

        //then -- 검증
        Assertions.assertThat(appointment.getComment()).isEqualTo("코를 높이고 싶어요");
        Assertions.assertThat(appointment.getUpdateAt()).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void 예약정보_수정_실패() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //임시 요청 생성
        CreateAppointmentRequest request1 = new CreateAppointmentRequest(member.getId(), hospital.getId(), "DENTAL", "눈이 건조해요", "이동엽", "01012345678");
        Long saveId = appointmentService.save(request1);

        UpdateAppointmentRequest request2 = new UpdateAppointmentRequest("DENTAL", null, "이동", "011");

        //when -- 동작
        Appointment appointment = appointmentService.findById(saveId);
        appointmentService.update(appointment.getId(), request2);


        //then -- 검증
        fail("코멘트가 비어있으므로 예외가 발생해야 한다.");
    }

    @Test
    public void paging() throws Exception {
        //given -- 조건

        //when -- 동작
        List<Appointment> list = appointmentService.findByMemberIdUsingPagingOrderByCreateAtAsc(1L, 0);

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }
}