package io.wisoft.capstonedesign.global.exception.duplicate;

import io.wisoft.capstonedesign.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateNicknameException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicateNicknameException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
