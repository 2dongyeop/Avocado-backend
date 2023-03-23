package io.wisoft.capstonedesign.global.exception.nullcheck;

public class NullReviewReplyException extends RuntimeException {
    public NullReviewReplyException() {
    }

    public NullReviewReplyException(String message) {
        super(message);
    }

    public NullReviewReplyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullReviewReplyException(Throwable cause) {
        super(cause);
    }

    public NullReviewReplyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
