package io.wisoft.capstonedesign.controller;

import io.wisoft.capstonedesign.domain.enumeration.AppointmentStatus;
import lombok.Getter;

@Getter
public class AppointmentSearch {

    private String memberNickName;
    private AppointmentStatus appointmentStatus;
}
