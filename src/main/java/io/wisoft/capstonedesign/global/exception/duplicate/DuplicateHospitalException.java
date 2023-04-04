package io.wisoft.capstonedesign.global.exception.duplicate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "duplicate entity")
public class DuplicateHospitalException extends RuntimeException {
    public DuplicateHospitalException() {
    }

    public DuplicateHospitalException(String message) {
        super(message);
    }

    public DuplicateHospitalException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateHospitalException(Throwable cause) {
        super(cause);
    }

    public DuplicateHospitalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
