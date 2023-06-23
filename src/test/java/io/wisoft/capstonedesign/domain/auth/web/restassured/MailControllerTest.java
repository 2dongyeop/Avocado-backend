package io.wisoft.capstonedesign.domain.auth.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.auth.application.EmailServiceImpl;
import io.wisoft.capstonedesign.domain.auth.web.dto.CertificateMailRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.MailObject;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MailControllerTest extends ApiTest {

    @Autowired
    private EmailServiceImpl emailService;

    @Nested
    @DisplayName("이메일 인증 코드 전송")
    public class SendCertificationCode {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 이메일로 인증 코드를 전송해야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String email = "SendCertificationCodeSuccess1@email.com";
            final MailObject mailObject = new MailObject(email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mailObject)
                    .when()
                    .post("/mail/certification-code")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("이메일 형식이 올바르지 않을 경우, 인증코드 전송에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String email = "fail1";
            final MailObject mailObject = new MailObject(email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mailObject)
                    .when()
                    .post("/mail/certification-code")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


        @Test
        @DisplayName("기존 회원과 이메일이 중복될 경우, 인증코드 전송에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String email = "ldy_1204@naver.com"; //사전데이터
            final MailObject mailObject = new MailObject(email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mailObject)
                    .when()
                    .post("/mail/certification-code")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("이메일 인증")
    public class CertificationEmail {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 이메일 인증에 완료해야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String email = "email-success1@naver.com";
            final String code = emailService.sendCertificationCode(email);

            final CertificateMailRequest request = new CertificateMailRequest(email, code);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/mail/certification-email")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("Redis에 해당 이메일로 보낸 코드 정보가 없을 경우, 인증에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String email = "email-fail1@naver.com";
            final String code = emailService.sendCertificationCode(email);

            final CertificateMailRequest request = new CertificateMailRequest("invalid-email", code);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/mail/certification-email")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }


        @Test
        @DisplayName("해당 이메일은 존재하지만 인증 코드가 일치하지 않을 경우, 인증에 실패한다.")
        public void 실패2() throws Exception {

            final String email = "email-fail1@naver.com";
            final String code = emailService.sendCertificationCode(email);

            final CertificateMailRequest request = new CertificateMailRequest(email, "invalid-code");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/mail/certification-email")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("CertificationCode is not match");
        }
    }


    @Nested
    @DisplayName("회원 비밀번호 찾기")
    public class ResetMemberPassword {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 임시비밀번호로 설정한 뒤 이메일을 전송한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String email = "ldy_1204@naver.com";
            final MailObject mailObject = new MailObject(email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mailObject)
                    .when()
                    .post("/mail/member/password")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }
    }


    @Nested
    @DisplayName("의료진 비밀번호 찾기")
    public class ResetStaffPassword {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 임시비밀번호로 설정한 뒤 이메일을 전송한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String email = "yjw9424@naver.com";
            final MailObject mailObject = new MailObject(email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mailObject)
                    .when()
                    .post("/mail/staff/password")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }
    }
}