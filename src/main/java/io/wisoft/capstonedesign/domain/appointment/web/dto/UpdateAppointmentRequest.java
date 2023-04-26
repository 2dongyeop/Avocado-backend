package io.wisoft.capstonedesign.domain.appointment.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateAppointmentRequest(
        @NotBlank String dept,
        @NotBlank String comment,
        @NotBlank String appointName,
        @NotBlank String appointPhonenumber
) { }