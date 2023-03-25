package io.wisoft.capstonedesign.domain.staff.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateStaffPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
