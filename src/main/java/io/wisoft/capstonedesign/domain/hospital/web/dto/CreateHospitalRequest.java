package io.wisoft.capstonedesign.domain.hospital.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateHospitalRequest {
    private String name;
    private String number;
    private String address;
    private String operatingTime;
}
