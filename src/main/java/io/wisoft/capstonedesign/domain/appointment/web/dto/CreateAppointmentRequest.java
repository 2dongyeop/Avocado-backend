package io.wisoft.capstonedesign.domain.appointment.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateAppointmentRequest(
        @NotNull Long memberId,
        @NotNull Long hospitalId,
        @NotBlank String dept,
        @NotBlank String comment,
        @NotBlank String appointName,
        @NotBlank String appointPhonenumber,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
        LocalDateTime appointmentDate
) {
}