package io.wisoft.capstonedesign.domain.appointment.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(
        @NotNull Long memberId,
        @NotNull Long hospitalId,
        @NotBlank String dept,
        @NotBlank String comment,
        @NotBlank String appointName,
        @NotBlank String appointPhonenumber
) {}