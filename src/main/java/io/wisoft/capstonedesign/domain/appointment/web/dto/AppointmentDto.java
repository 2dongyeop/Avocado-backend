package io.wisoft.capstonedesign.domain.appointment.web.dto;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentDto {
    private String hospital;
    private String dept;
    private String comment;
    private String appointName;
    private String appointPhonenumber;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public AppointmentDto(final Appointment appointment) {
        this.hospital = appointment.getHospital().getName();
        this.dept = appointment.getDept().toString();
        this.comment = appointment.getComment();
        this.appointName = appointment.getAppointName();
        this.appointPhonenumber = appointment.getAppointPhonenumber();
        this.createAt = appointment.getCreateAt();
        this.updateAt = appointment.getUpdateAt();
    }
}