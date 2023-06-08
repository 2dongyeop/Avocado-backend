package io.wisoft.capstonedesign.domain.appointment.persistence;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Test
    public void update_success() throws Exception {
        //given -- 조건
        final Appointment appointment = defaultAppointment();

        //when -- 동작
        appointment.update(
                HospitalDept.INTERNAL_MEDICINE,
                "update-comment",
                "update-name",
                "update-phonenumber"
        );

        //then -- 검증
        Assertions.assertThat(appointment.getComment()).isEqualTo("update-comment");
        Assertions.assertThat(appointment.getAppointName()).isEqualTo("update-name");
        Assertions.assertThat(appointment.getAppointPhonenumber()).isEqualTo("update-phonenumber");

    }

    private Appointment defaultAppointment() {

        return Appointment.builder()
                .member(Member.builder()
                        .nickname("nickname")
                        .email("email")
                        .password("password")
                        .phoneNumber("phonenumber")
                        .build())
                .hospital(Hospital.builder()
                        .name("hosp-name")
                        .number("number")
                        .address("address")
                        .operatingTime("operatingTime")
                        .build())
                .dept(HospitalDept.DENTAL)
                .comment("comment")
                .appointName("appointmentName")
                .appointPhonenumber("appointPhonenumber")
                .build();
    }


}