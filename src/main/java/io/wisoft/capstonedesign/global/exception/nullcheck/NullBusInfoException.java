package io.wisoft.capstonedesign.global.exception.nullcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NullBusInfoException extends RuntimeException {
    public NullBusInfoException() {
    }

    public NullBusInfoException(String message) {
        super(message);
    }

    public NullBusInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullBusInfoException(Throwable cause) {
        super(cause);
    }

    public NullBusInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
