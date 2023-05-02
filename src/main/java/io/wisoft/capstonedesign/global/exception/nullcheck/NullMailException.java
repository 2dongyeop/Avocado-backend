package io.wisoft.capstonedesign.global.exception.nullcheck;

public class NullMailException extends RuntimeException {
    public NullMailException() {
    }

    public NullMailException(String message) {
        super(message);
    }

    public NullMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullMailException(Throwable cause) {
        super(cause);
    }

    protected NullMailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
