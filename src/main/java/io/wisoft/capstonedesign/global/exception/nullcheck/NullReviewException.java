package io.wisoft.capstonedesign.global.exception.nullcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NullReviewException extends RuntimeException {
    public NullReviewException() {
    }

    public NullReviewException(String message) {
        super(message);
    }

    public NullReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullReviewException(Throwable cause) {
        super(cause);
    }

    public NullReviewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
