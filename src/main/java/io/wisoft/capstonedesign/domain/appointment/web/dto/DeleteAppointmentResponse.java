package io.wisoft.capstonedesign.domain.appointment.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteAppointmentResponse {
    private Long id;


    public DeleteAppointmentResponse(final Long id) {
        this.id = id;
    }
}