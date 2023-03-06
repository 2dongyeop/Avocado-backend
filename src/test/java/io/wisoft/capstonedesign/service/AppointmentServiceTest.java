package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Appointment;
import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.domain.enumeration.AppointmentStatus;
import io.wisoft.capstonedesign.repository.AppointmentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AppointmentServiceTest {

    @Autowired AppointmentService appointmentService;
    @Autowired AppointmentRepository appointmentRepository;

    //예약 저장
    @Test
    public void 예약_저장() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        //병원 생성
        Hospital hospital = Hospital.createHospital("아보카도병원", "0420000000", "대전 유성구", "365일 연중 무휴");
        //예약 생성
        Appointment appointment = Appointment.createAppointment(member, hospital, "안과", "눈이 건조해요", "이동엽", "01012345678");

        //when -- 동작
        Long saveId = appointmentService.save(appointment);

        //then -- 검증
        Assertions.assertThat(appointment).isEqualTo(appointmentRepository.findOne(saveId));
    }

    //예약 취소
    @Test
    public void 예약_취소() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        //병원 생성
        Hospital hospital = Hospital.createHospital("아보카도병원", "0420000000", "대전 유성구", "365일 연중 무휴");
        //예약 생성 후 저장
        Appointment appointment = Appointment.createAppointment(member, hospital, "안과", "눈이 건조해요", "이동엽", "01012345678");
        Long saveId = appointmentService.save(appointment);

        //when -- 동작
        Appointment getAppointment = appointmentRepository.findOne(saveId);
        getAppointment.cancel();

        //then -- 검증
        Assertions.assertThat(getAppointment.getStatus()).isEqualTo(AppointmentStatus.CANCEL);
    }

    //예약 중복 취소 요청
    @Test(expected = IllegalStateException.class)
    public void 예약_중복_취소요청() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        //병원 생성
        Hospital hospital = Hospital.createHospital("아보카도병원", "0420000000", "대전 유성구", "365일 연중 무휴");
        //예약 생성 후 저장
        Appointment appointment = Appointment.createAppointment(member, hospital, "안과", "눈이 건조해요", "이동엽", "01012345678");
        Long saveId = appointmentService.save(appointment);

        //when -- 동작
        appointmentService.cancelAppointment(saveId);
        appointmentService.cancelAppointment(saveId);

        //then -- 검증
        fail("중복 예약 취소 요청으로 인한 예외가 발생해야 한다.");
    }
}