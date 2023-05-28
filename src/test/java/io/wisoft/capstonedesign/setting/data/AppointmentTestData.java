package io.wisoft.capstonedesign.setting.data;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;

public class AppointmentTestData {
    public static Appointment getDefaultAppointment(Member member, Hospital hospital) {
        return Appointment.builder()
                .member(member)
                .hospital(hospital)
                .dept(HospitalDept.valueOf("DENTAL"))
                .comment("test comment")
                .appointName("test appointName")
                .appointPhonenumber("test appointPhonenumber")
                .build();
    }
}
