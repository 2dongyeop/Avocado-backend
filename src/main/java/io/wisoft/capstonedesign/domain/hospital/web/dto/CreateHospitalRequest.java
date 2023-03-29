package io.wisoft.capstonedesign.domain.hospital.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateHospitalRequest {
    @NotBlank private String name;
    @NotBlank private String number;
    @NotBlank private String address;
    @NotBlank private String operatingTime;
}
