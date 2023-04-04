package io.wisoft.capstonedesign.global.exception.nullcheck;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class NullHospitalException extends RuntimeException {
    public NullHospitalException() {
    }

    public NullHospitalException(String message) {
        super(message);
    }

    public NullHospitalException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullHospitalException(Throwable cause) {
        super(cause);
    }

    public NullHospitalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
