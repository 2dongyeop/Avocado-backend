package io.wisoft.capstonedesign.global.exception.token;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ExpiredTokenException extends RuntimeException {

    private final ErrorCode errorCode;

    public ExpiredTokenException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
