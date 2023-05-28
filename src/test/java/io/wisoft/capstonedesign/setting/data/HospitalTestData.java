package io.wisoft.capstonedesign.global.data;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;

public class HospitalTestData {
    public static Hospital getDefaultHospital() {
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        return hospital;
    }
}
