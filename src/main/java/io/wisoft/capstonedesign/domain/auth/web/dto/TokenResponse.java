package io.wisoft.capstonedesign.domain.auth.web.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String tokenType;

    public TokenResponse(final String accessToken, final String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
}
