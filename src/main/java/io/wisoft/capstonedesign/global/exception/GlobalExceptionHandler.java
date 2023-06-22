package io.wisoft.capstonedesign.global.exception;

import io.jsonwebtoken.JwtException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateEmailException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateHospitalException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateNicknameException;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalDeptException;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.global.exception.token.AlreadyLogoutException;
import io.wisoft.capstonedesign.global.exception.token.ExpiredTokenException;
import io.wisoft.capstonedesign.global.exception.token.InvalidTokenException;
import io.wisoft.capstonedesign.global.exception.token.NotExistTokenException;
import io.wisoft.capstonedesign.global.slack.SlackConstant;
import io.wisoft.capstonedesign.global.slack.SlackErrorMessage;
import io.wisoft.capstonedesign.global.slack.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackService slackService;

    @Qualifier("asyncExecutor")
    private final ThreadPoolTaskExecutor executor;

    /**
     * @Builder 사용으로 인해 요구된 파라미터 유효성 검사시
     * Assert.nonNull, Assert.hasText 등을 통해 발생되는 예외
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException exception) {

        log.error("handleIllegalArgumentException", exception);
        final ErrorResponse response = new ErrorResponse(ErrorCode.ASSERT_INVALID_INPUT);

        slackService.sendSlackMessage(new SlackErrorMessage(LocalDateTime.now(), response.getMessage()), SlackConstant.ERROR_CHANNEL);
        return new ResponseEntity<>(response, response.getHttpStatusCode());
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(final IllegalStateException exception) {

        log.error("handleIllegalArgumentException", exception);
        final ErrorResponse response = new ErrorResponse(ErrorCode.ILLEGAL_STATE);

        slackService.sendSlackMessage(new SlackErrorMessage(LocalDateTime.now(), response.getMessage()), SlackConstant.ERROR_CHANNEL);
        return new ResponseEntity<>(response, response.getHttpStatusCode());
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
     * Dept 불일치 예외
     */
    @ExceptionHandler(IllegalDeptException.class)
    public ResponseEntity<ErrorResponse> handleIllegalDept(final IllegalDeptException exception) {

        log.error("일치하는 dept가 존재하지 않습니다.", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    /**
     * java.util.concurrent.TimeoutException
     * 제한시간을 지나 타임아웃시 발생하는 예외
     */
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeoutException(final TimeoutException exception) {

        log.error("handleTimeoutException", exception);
        return getErrorResponseResponseEntity(ErrorCode.TIME_OUT);
    }


    /**
     * JWT 토큰이 적재되지 않았을 경우
     */
    @ExceptionHandler(NotExistTokenException.class)
    public ResponseEntity<ErrorResponse> handleNotExistTokenException(final NotExistTokenException exception) {

        log.error("handleNotExistTokenException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    /**
     * JWT 토큰이 만료되었을 경우
     */
    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponse> handleExpiredTokenException(final ExpiredTokenException exception) {

        log.error("handleExpiredTokenException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    /**
     * JWT 토큰이 유효하지 않을 경우
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(final InvalidTokenException exception) {

        log.error("handleInvalidTokenException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    /**
     * 로그아웃된 토큰으로 요청할 경우
     */
    @ExceptionHandler(AlreadyLogoutException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyLogoutException(final AlreadyLogoutException exception) {

        log.error("handleAlreadyLogoutException", exception);
        return getErrorResponseResponseEntity(exception.getErrorCode());
    }


    /**
     * JWT exception
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(final JwtException exception) {

        log.error("handleJwtException", exception);
        return getErrorResponseResponseEntity(ErrorCode.JWT_EXCEPTION);
    }


    /**
     * 가장 흔히 발생하는 예외
     */
    @ExceptionHandler(IllegalValueException.class)
    public ResponseEntity<ErrorResponse> handlerIllegalValueException(final IllegalValueException exception) {

        log.error("handlerIllegalValueException", exception);
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
    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(final ErrorCode errorCode) {
        final ErrorResponse response = new ErrorResponse(errorCode);

        executor.execute(() -> {
            slackService.sendSlackMessage(new SlackErrorMessage(LocalDateTime.now(), errorCode.getMessage()), SlackConstant.ERROR_CHANNEL);
        });

        return new ResponseEntity<>(response, errorCode.getHttpStatusCode());
    }
}
