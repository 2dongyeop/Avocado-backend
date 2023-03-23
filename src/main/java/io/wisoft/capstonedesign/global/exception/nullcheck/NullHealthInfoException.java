package io.wisoft.capstonedesign.global.exception.nullcheck;

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
