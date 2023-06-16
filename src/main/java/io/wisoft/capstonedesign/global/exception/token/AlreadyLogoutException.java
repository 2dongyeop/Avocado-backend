package io.wisoft.capstonedesign.global.exception.token;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyLogoutException extends RuntimeException {

    private final ErrorCode errorCode;

    public AlreadyLogoutException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
