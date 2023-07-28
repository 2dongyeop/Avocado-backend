package io.wisoft.capstonedesign.domain.auth.web.dto;

public record TokenResponse(
        Long memberId,
        String tokenType,
        String accessToken,
        String refreshToken
) {
}