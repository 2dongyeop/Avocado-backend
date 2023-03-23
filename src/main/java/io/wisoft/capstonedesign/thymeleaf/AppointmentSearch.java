package io.wisoft.capstonedesign.thymeleaf;

import io.wisoft.capstonedesign.global.enumeration.status.AppointmentStatus;
import lombok.Getter;

@Getter
public class AppointmentSearch {

    private String memberNickName;
    private AppointmentStatus appointmentStatus;
}
