package io.wisoft.capstonedesign.domain.appointment.web;

import io.wisoft.capstonedesign.domain.appointment.web.dto.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AppointmentApiControllerTest {

    @Autowired AppointmentApiController appointmentApiController;

    @Test
    public void createAppointment_success() throws Exception {
        //given -- 조건
        final CreateAppointmentRequest request = CreateAppointmentRequest.builder()
                .memberId(1L)
                .hospitalId(1L)
                .dept("DENTAL")
                .comment("test-comment")
                .appointName("test-appointName")
                .appointPhonenumber("test-phonenumber")
                .build();

        //when -- 동작
        final CreateAppointmentResponse appointment = appointmentApiController.createAppointment(request);

        //then -- 검증
        Assertions.assertThat(appointment.id()).isNotNull();
    }

    @Test
    public void deleteAppointment_success() throws Exception {
        //given -- 조건
        final CreateAppointmentRequest request = CreateAppointmentRequest.builder()
                .memberId(1L)
                .hospitalId(1L)
                .dept("DENTAL")
                .comment("test-comment")
                .appointName("test-appointName")
                .appointPhonenumber("test-phonenumber")
                .build();

        final CreateAppointmentResponse appointment = appointmentApiController.createAppointment(request);

        //when -- 동작
        final DeleteAppointmentResponse deleteAppointmentResponse = appointmentApiController.deleteAppointment(appointment.id());

        //then -- 검증
        Assertions.assertThat(deleteAppointmentResponse.id()).isNotNull();
    }

    @Test
    public void updateAppointment_success() throws Exception {
        //given -- 조건
        final CreateAppointmentRequest request = CreateAppointmentRequest.builder()
                .memberId(1L)
                .hospitalId(1L)
                .dept("DENTAL")
                .comment("test-comment")
                .appointName("test-appointName")
                .appointPhonenumber("test-phonenumber")
                .build();

        final CreateAppointmentResponse appointment = appointmentApiController.createAppointment(request);

        //when -- 동작
        UpdateAppointmentResponse response = appointmentApiController.updateAppointment(appointment.id(), UpdateAppointmentRequest.builder()
                .dept("DENTAL")
                .comment("update-comment")
                .appointName("update-name")
                .appointPhonenumber("update-phonenumber")
                .build());


        //then -- 검증
        Assertions.assertThat(response.id()).isNotNull();
        Assertions.assertThat(response.id()).isGreaterThan(0);
    }

    @Test
    public void appointment_success() throws Exception {
        //given -- 조건

        final CreateAppointmentRequest request = CreateAppointmentRequest.builder()
                .memberId(1L)
                .hospitalId(1L)
                .dept("DENTAL")
                .comment("test-comment")
                .appointName("test-appointName")
                .appointPhonenumber("test-phonenumber")
                .build();

        final CreateAppointmentResponse appointment = appointmentApiController.createAppointment(request);

        //when -- 동작
        Result result = appointmentApiController.appointment(appointment.id());

        //then -- 검증
        Assertions.assertThat(result).isNotNull();
    }
}