package io.wisoft.capstonedesign.global.exception.duplicate;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicateEmailException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
