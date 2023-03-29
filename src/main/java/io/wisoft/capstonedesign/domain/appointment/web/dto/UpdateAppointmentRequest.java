package io.wisoft.capstonedesign.domain.appointment.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateAppointmentRequest {
    @NotBlank private String dept;
    @NotBlank private String comment;
    @NotBlank private String appointName;
    @NotBlank private String appointPhonenumber;
}
