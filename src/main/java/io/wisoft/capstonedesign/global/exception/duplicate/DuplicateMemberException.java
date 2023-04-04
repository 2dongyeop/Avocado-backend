package io.wisoft.capstonedesign.global.exception.duplicate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "duplicate entity")
public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException() {
        super();
    }

    public DuplicateMemberException(String message) {
        super(message);
    }

    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateMemberException(Throwable cause) {
        super(cause);
    }

    protected DuplicateMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
