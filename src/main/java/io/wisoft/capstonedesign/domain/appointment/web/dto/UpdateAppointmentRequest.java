package io.wisoft.capstonedesign.domain.appointment.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateAppointmentRequest {
    private String dept;
    private String comment;
    private String appointName;
    private String appointPhonenumber;
}
