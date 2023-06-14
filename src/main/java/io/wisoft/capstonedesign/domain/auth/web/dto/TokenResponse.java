package io.wisoft.capstonedesign.domain.auth.web.dto;

public record TokenResponse(
        String tokenType,
        String accessToken,
        String refreshToken
) {
}