package io.wisoft.capstonedesign.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int httpStatusCode;
    private final String errorCode;
    private final String message;

    public ErrorResponse(final ErrorCode errorCode) {
        this.httpStatusCode = errorCode.getHttpStatusCode();
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }
}
