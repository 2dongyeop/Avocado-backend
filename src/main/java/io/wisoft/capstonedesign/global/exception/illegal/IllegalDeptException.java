package io.wisoft.capstonedesign.global.exception.illegal;

public class IllegalDeptException extends RuntimeException {
    public IllegalDeptException() {
    }

    public IllegalDeptException(String message) {
        super(message);
    }

    public IllegalDeptException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDeptException(Throwable cause) {
        super(cause);
    }

    public IllegalDeptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
