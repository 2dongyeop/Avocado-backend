package io.wisoft.capstonedesign.global.data;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;

public class StaffTestData {
    public static Staff getDefaultStaff(final Hospital hospital) {
        return Staff.builder()
                .hospital(hospital)
                .name("name1")
                .email("email1")
                .password("pass1")
                .license_path("licen1")
                .dept(HospitalDept.DENTAL)
                .build();
    }
}
