package io.wisoft.capstonedesign.domain.appointment.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAppointmentRequest {
    @NotNull private Long memberId;
    @NotNull private Long hospitalId;
    @NotBlank private String dept;
    @NotBlank private String comment;
    @NotBlank private String appointName;
    @NotBlank private String appointPhonenumber;
}
