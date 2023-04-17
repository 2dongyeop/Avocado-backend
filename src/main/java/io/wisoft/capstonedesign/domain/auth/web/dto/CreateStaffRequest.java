package io.wisoft.capstonedesign.domain.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateStaffRequest(
        @NotNull Long hospitalId,
        @NotBlank String name,
        @NotBlank @Email(message = "이메일 형식에 맞지 않습니다.") String email,
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
                message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.")
        @NotBlank String password1,
        @NotBlank String password2,
        @NotBlank String licensePath,
        @NotBlank String dept,
        @NotBlank String code
) { }