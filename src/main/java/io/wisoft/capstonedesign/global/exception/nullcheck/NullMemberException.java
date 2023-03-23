package io.wisoft.capstonedesign.global.exception.nullcheck;

public class NullMemberException extends RuntimeException {
    public NullMemberException() {
    }

    public NullMemberException(String message) {
        super(message);
    }

    public NullMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullMemberException(Throwable cause) {
        super(cause);
    }

    public NullMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
