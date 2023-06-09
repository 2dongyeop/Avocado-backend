package io.wisoft.capstonedesign.global.exception.illegal;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class IllegalDeptException extends RuntimeException {

    private final ErrorCode errorCode;

    public IllegalDeptException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
