package io.wisoft.capstonedesign.domain.staff.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateStaffPasswordRequest {
    @NotBlank private String oldPassword;
    @NotBlank private String newPassword;
}
