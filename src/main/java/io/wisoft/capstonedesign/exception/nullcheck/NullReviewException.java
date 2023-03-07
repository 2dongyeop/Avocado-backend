package io.wisoft.capstonedesign.exception.nullcheck;

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
