package io.wisoft.capstonedesign.global.exception.nullcheck;

public class NullBoardException extends RuntimeException {
    public NullBoardException() {
    }

    public NullBoardException(String message) {
        super(message);
    }

    public NullBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullBoardException(Throwable cause) {
        super(cause);
    }

    public NullBoardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
