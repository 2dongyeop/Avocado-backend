package io.wisoft.capstonedesign.global.exception.nullcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NullHealthInfoException extends RuntimeException {
    public NullHealthInfoException() {
    }

    public NullHealthInfoException(String message) {
        super(message);
    }

    public NullHealthInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullHealthInfoException(Throwable cause) {
        super(cause);
    }

    public NullHealthInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
