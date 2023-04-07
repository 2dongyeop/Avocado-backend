package io.wisoft.capstonedesign.domain.staff.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateStaffRequest {
    @NotNull private Long hospitalId;
    @NotBlank private String name;
    @NotBlank private String email;
    @NotBlank private String password1;
    @NotBlank private String password2;
    @NotBlank private String licensePath;
    @NotBlank private String dept;
}
