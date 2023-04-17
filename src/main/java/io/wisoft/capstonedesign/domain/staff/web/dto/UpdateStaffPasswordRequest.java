package io.wisoft.capstonedesign.domain.staff.web.dto;


import jakarta.validation.constraints.NotBlank;

public record UpdateStaffPasswordRequest(
        @NotBlank String oldPassword,
        @NotBlank String newPassword) { }