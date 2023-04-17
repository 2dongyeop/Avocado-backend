package io.wisoft.capstonedesign.domain.staff.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateStaffHospitalRequest(@NotBlank String hospitalName) { }