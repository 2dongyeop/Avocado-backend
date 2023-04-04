package io.wisoft.capstonedesign.global.exception.nullcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NullAppointmentException extends RuntimeException {
    public NullAppointmentException() {
    }

    public NullAppointmentException(String message) {
        super(message);
    }

    public NullAppointmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullAppointmentException(Throwable cause) {
        super(cause);
    }

    public NullAppointmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
