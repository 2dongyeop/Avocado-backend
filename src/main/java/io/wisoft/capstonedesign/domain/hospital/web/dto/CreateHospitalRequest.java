package io.wisoft.capstonedesign.domain.hospital.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateHospitalRequest(
        @NotBlank String name,
        @NotBlank String number,
        @NotBlank String address,
        @NotBlank String operatingTime
) { }