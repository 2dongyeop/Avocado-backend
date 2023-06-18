package io.wisoft.capstonedesign.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final HttpStatus httpStatusCode;
    private final String errorCode;
    private final String message;

    public ErrorResponse(final ErrorCode errorCode) {
        this.httpStatusCode = errorCode.getHttpStatusCode();
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }
}
