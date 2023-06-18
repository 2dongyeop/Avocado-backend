package io.wisoft.capstonedesign.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorCode {

    //COMMON
    //@Builder 사용으로 인한 Assert 검증시 발생
    ASSERT_INVALID_INPUT(BAD_REQUEST, "Common-Builder-400", "Builder가 요구하는 필수 파라미터가 요구되지 않았습니다."),
    NOT_FOUND(BAD_REQUEST, "Common-NotFound-404", "해당 엔티티를 찾을 수 없습니다."),
    TIME_OUT(REQUEST_TIMEOUT, "Common-TimeOut-408", "Timeout 발생"),


    //DUPLICATE
    DUPLICATE_EMAIL(BAD_REQUEST, "Duplicate-Mail-400", "Email is Duplicated"),
    DUPLICATE_HOSPITAL(BAD_REQUEST, "Duplicate-Hospital-400", "HospitalName is Duplicated"),


    //ILLEGAL
    ILLEGAL_HOSPITAL_DEPT(BAD_REQUEST, "Illegal-Dept-400", "HospitalDept is invalid"),
    ILLEGAL_AREA(BAD_REQUEST, "Illegal-Area-400", "Area is invalid"),
    ILLEGAL_DATE(BAD_REQUEST, "Illegal-Date-400", "Date is invalid"),
    ILLEGAL_PASSWORD(BAD_REQUEST, "Illegal-Password-400", "Password is not match"),
    ILLEGAL_CODE(BAD_REQUEST, "Illegal-Code-400", "CertificationCode is not match"),
    ILLEGAL_PARAM(BAD_REQUEST, "Illegal-Param-400", "Parameter is empty"),
    ILLEGAL_STATE(BAD_REQUEST, "Illegal-State-400", "State is something wrong"),
    ILLEGAL_STAR_POINT(BAD_REQUEST, "Illegal-StarPoint-400", "StarPoint between 1 and 5"),
    INVALID_TOKEN(FORBIDDEN, "Illegal-Invalid-Token-401", "Token is invalid"),
    NOT_EXIST_TOKEN(UNAUTHORIZED, "Illegal-Not-Exist-Token-401", "Token is not exist"),
    EXPIRED_TOKEN(UNAUTHORIZED, "Illegal-Expired-Token-401", "Token is expired"),
    ALREADY_LOGOUT_TOKEN(FORBIDDEN, "Token-403", "Already logout token"),
    JWT_EXCEPTION(UNAUTHORIZED, "Token-400", "JWT is invalid");


    private HttpStatus httpStatusCode;
    private String errorCode;
    private String message;

    ErrorCode(final HttpStatus httpStatusCode, final String errorCode, final String message) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = message;
    }
}
