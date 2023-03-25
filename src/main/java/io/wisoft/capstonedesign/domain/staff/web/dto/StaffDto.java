package io.wisoft.capstonedesign.domain.staff.web.dto;

import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffDto {
    private String name;
    private String email;
    private String dept;
    private String hospital;

    public StaffDto(final Staff staff) {
        this.name = staff.getName();
        this.email = staff.getEmail();
        this.dept = String.valueOf(staff.getDept());
        this.hospital = staff.getHospital().getName();
    }
}
