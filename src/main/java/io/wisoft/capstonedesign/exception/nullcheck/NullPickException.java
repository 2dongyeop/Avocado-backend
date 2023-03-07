package io.wisoft.capstonedesign.exception.nullcheck;

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
