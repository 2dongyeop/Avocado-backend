package io.wisoft.capstonedesign.global.exception.nullcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NullStaffException extends RuntimeException {
    public NullStaffException() {
    }

    public NullStaffException(String message) {
        super(message);
    }

    public NullStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullStaffException(Throwable cause) {
        super(cause);
    }

    public NullStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
