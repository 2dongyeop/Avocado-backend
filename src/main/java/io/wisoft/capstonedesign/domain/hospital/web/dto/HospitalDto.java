package io.wisoft.capstonedesign.domain.hospital.web.dto;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalDto {
    private String name;
    private String address;
    private String number;
    private String operatingTime;

    public HospitalDto(final Hospital hospital) {
        name = hospital.getName();
        address = hospital.getAddress();
        number = hospital.getNumber();
        operatingTime = hospital.getOperatingTime();
    }
}
