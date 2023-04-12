package io.wisoft.capstonedesign.global.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "토큰이 존재하지 않음")
public class NotExistTokenException extends RuntimeException {
    public NotExistTokenException() {
        super();
    }

    public NotExistTokenException(String message) {
        super(message);
    }

    public NotExistTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistTokenException(Throwable cause) {
        super(cause);
    }

    protected NotExistTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
