package io.wisoft.capstonedesign.domain.appointment.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAppointmentRequest {
    private Long memberId;
    private Long hospitalId;
    private String dept;
    private String comment;
    private String appointName;
    private String appointPhonenumber;
}
