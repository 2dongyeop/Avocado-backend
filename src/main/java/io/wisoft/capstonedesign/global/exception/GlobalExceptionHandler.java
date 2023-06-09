package io.wisoft.capstonedesign.global.exception;

import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateEmailException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateHospitalException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateNicknameException;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * @Builder 사용으로 인해 요구된 파라미터 유효성 검사시
     * Assert.nonNull, Assert.hasText 등을 통해 발생되는 예외
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException exception) {

        log.error("handleIllegalArgumentException", exception);
        final ErrorResponse response = new ErrorResponse(ErrorCode.ASSERT_INVALID_INPUT);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getHttpStatusCode()));
    }


    /**
     * 조회 실패시 발생하는 예외
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException exception) {

        log.error("handleNotFoundException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    /**
     * 중복 예외
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicateEmailException(final DuplicateEmailException exception) {

        log.error("handlerDuplicateEmailException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicateNicknameException(final DuplicateNicknameException exception) {

        log.error("handlerDuplicateNicknameException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    @ExceptionHandler(DuplicateHospitalException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicateHospitalException(final DuplicateHospitalException exception) {

        log.error("handlerDuplicateHospitalException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    @NotNull
    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(final ErrorCode exception) {
        final ErrorResponse response = new ErrorResponse(exception);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(exception.getHttpStatusCode()));
    }
}
