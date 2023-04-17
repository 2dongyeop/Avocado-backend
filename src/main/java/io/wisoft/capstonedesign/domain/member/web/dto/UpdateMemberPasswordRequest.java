package io.wisoft.capstonedesign.domain.member.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMemberPasswordRequest(
        @NotBlank String oldPassword,
        @NotBlank String newPassword
) { }