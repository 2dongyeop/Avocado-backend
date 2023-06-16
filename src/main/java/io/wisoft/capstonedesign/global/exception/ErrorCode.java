package io.wisoft.capstonedesign.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorCode {

    //COMMON
    //@Builder 사용으로 인한 Assert 검증시 발생
    ASSERT_INVALID_INPUT(400, "Common-Builder-400", "Builder가 요구하는 필수 파라미터가 요구되지 않았습니다."),
    NOT_FOUND(404, "Common-NotFound-404", "해당 엔티티를 찾을 수 없습니다."),
    TIME_OUT(500, "Common-TimeOut-500", "Timeout 발생"),


    //DUPLICATE
    DUPLICATE_EMAIL(400, "Duplicate-Mail-400", "Email is Duplicated"),
    DUPLICATE_HOSPITAL(400, "Duplicate-Hospital-400", "HospitalName is Duplicated"),


    //ILLEGAL
    ILLEGAL_HOSPITAL_DEPT(400, "Illegal-Dept-400", "HospitalDept is invalid"),
    ILLEGAL_AREA(400, "Illegal-Area-400", "Area is invalid"),
    ILLEGAL_DATE(400, "Illegal-Date-400", "Date is invalid"),
    ILLEGAL_PASSWORD(400, "Illegal-Password-400", "Password is not match"),
    ILLEGAL_CODE(400, "Illegal-Code-400", "CertificationCode is not match"),
    ILLEGAL_PARAM(400, "Illegal-Param-400", "Parameter is empty"),
    ILLEGAL_STATE(400, "Illegal-State-400", "State is something wrong"),
    ILLEGAL_STAR_POINT(400, "Illegal-StarPoint-400", "StarPoint between 1 and 5"),
    INVALID_TOKEN(403, "Illegal-Invalid-Token-401", "Token is invalid"),
    NOT_EXIST_TOKEN(401, "Illegal-Not-Exist-Token-401", "Token is not exist"),
    EXPIRED_TOKEN(401, "Illegal-Expired-Token-401", "Token is expired"),
    ALREADY_LOGOUT_TOKEN(403, "Token-403", "Already logout token"),
    JWT_EXCEPTION(400, "Token-400", "JWT is invalid");


    private int httpStatusCode;
    private String errorCode;
    private String message;

    ErrorCode(final int httpStatusCode, final String errorCode, final String message) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = message;
    }
}
