package io.wisoft.capstonedesign.global.exception.illegal;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class IllegalValueException extends RuntimeException {

    private final ErrorCode errorCode;

    public IllegalValueException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
