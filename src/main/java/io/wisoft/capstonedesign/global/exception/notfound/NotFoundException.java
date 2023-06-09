package io.wisoft.capstonedesign.global.exception.notfound;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotFoundException() {
        this.errorCode = ErrorCode.NOT_FOUND;
    }

    public NotFoundException(final String message) {
        super(message);
        this.errorCode = ErrorCode.NOT_FOUND;
    }
}
