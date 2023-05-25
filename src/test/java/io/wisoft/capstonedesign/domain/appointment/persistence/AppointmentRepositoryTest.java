package io.wisoft.capstonedesign.domain.appointment.persistence;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullAppointmentException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static io.wisoft.capstonedesign.global.data.AppointmentTestData.getDefaultAppointment;
import static io.wisoft.capstonedesign.global.data.HospitalTestData.getDefaultHospital;
import static io.wisoft.capstonedesign.global.data.MemberTestData.getDefaultMember;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AppointmentRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    public void 예약_단건_상세_조회() throws Exception {
        //given -- 조건
        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //예약 생성
        final Appointment appointment = getDefaultAppointment(member, hospital);

        final Appointment savedAppointment = appointmentRepository.save(appointment);

        //when -- 동작
        final Appointment findAppointment = appointmentRepository.findDetailById(savedAppointment.getId())
                .orElseThrow(NullAppointmentException::new);

        //then -- 검증
        Assertions.assertThat(findAppointment).isEqualTo(savedAppointment);
    }


    @Test
    public void 예약_단건_상세_조회_실패_존재하지않는_id() throws Exception {

        //given -- 조건
        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //예약 생성
        final Appointment appointment = getDefaultAppointment(member, hospital);

        appointmentRepository.save(appointment);

        //when -- 동작
        assertThrows(NullAppointmentException.class, () -> {
            appointmentRepository.findDetailById(100L).orElseThrow(NullAppointmentException::new);
        });

    }

    @Test
    public void 예약_단건_상세_조회_실패_일치하지_않는_id() throws Exception {

        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //예약 생성
        final Appointment appointment = getDefaultAppointment(member, hospital);

        final Appointment savedAppointment = appointmentRepository.save(appointment);

        //when -- 동작
        final Appointment findAppointment = appointmentRepository.findDetailById(1L)
                .orElseThrow(NullAppointmentException::new);

        //then
        Assertions.assertThat(findAppointment).isNotEqualTo(savedAppointment);
    }
}