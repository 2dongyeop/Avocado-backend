package io.wisoft.capstonedesign.domain.auth.web.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank String email,
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
                message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.")
        @NotBlank String password) { }