package io.wisoft.capstonedesign.domain.auth.web.dto;

public record LoginResponse(
        Long memberId,
        String tokenType,
        String accessToken,
        String refreshToken,
        String name
) {
}