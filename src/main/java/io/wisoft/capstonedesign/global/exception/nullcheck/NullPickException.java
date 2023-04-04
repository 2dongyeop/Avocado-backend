package io.wisoft.capstonedesign.global.exception.nullcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NullPickException extends RuntimeException {
    public NullPickException() {
    }

    public NullPickException(String message) {
        super(message);
    }

    public NullPickException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullPickException(Throwable cause) {
        super(cause);
    }

    public NullPickException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
