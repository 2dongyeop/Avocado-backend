package io.wisoft.capstonedesign.domain.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CertificateMailRequest(
        @Email(message = "이메일 형식에 맞지 않습니다.")
        @Size(min = 1, message = "Please, set an email address to send the message to it")
        @NotNull String email,
        @NotNull String code) { }