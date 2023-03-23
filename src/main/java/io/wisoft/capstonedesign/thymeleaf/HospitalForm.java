package io.wisoft.capstonedesign.thymeleaf;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HospitalForm {

    @NotEmpty(message = "병원 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "병원 전화번호는 필수입니다.")
    private String number;

    @NotEmpty(message = "병원 주소는 필수입니다.")
    private String address;

    @NotEmpty(message = "병원 운영시간은 필수입니다.")
    private String operatingTime;
}
