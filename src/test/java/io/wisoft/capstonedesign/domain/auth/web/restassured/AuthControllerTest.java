package io.wisoft.capstonedesign.domain.auth.web.restassured;


import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.auth.persistence.DBMailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.AuthController;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffResponse;
import io.wisoft.capstonedesign.domain.auth.web.dto.LoginRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthControllerTest extends ApiTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private MailAuthenticationRepository mailAuthenticationRepository;


    @Nested
    @DisplayName("회원가입")
    public class SignUp {

        @Test
        @DisplayName("회원가입 요청시 성공적으로 DB에 회원이 저장이 되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "회원가입성공";
            final String email = "회원가입성공@email.com";

            이메일인증성공상태로설정(email);

            final var request = getCreateMemberRequest(nickname, email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("이메일 형식이 맞지 않을 경우 회원가입에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "회원가입실패1";
            final String email = "회원가입실패1";

            이메일인증성공상태로설정(email);

            final var request = getCreateMemberRequest(nickname, email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("error")).isEqualTo("Bad Request");
        }

        @Test
        @DisplayName("비밀번호와 확인비밀번호가 동일하지 않으면 회원가입에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String nickname = "회원가입실패2";
            final String email = "회원가입실패2@email.com";

            이메일인증성공상태로설정(email);

            final var request = new CreateMemberRequest(
                    nickname,
                    email,
                    "inputPassword",
                    "mismatch-password!!!!!!",
                    "phonenumber"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("error")).isEqualTo("Bad Request");
        }

        @Test
        @DisplayName("이메일 인증이 되어있지 않으면 회원가입에 실패한다.")
        public void 실패3() throws Exception {

            //given -- 조건
            final String nickname = "회원가입실패3";
            final String email = "회원가입실패3@email.com";

            final var request = getCreateMemberRequest(nickname, email);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("기존 회원과 닉네임이 중복될 경우 회원가입에 실패한다.")
        public void 실패4() throws Exception {

            //given -- 조건
            final String nickname = "이동엽"; //사전데이터
            final String email = "회원가입실패4@email.com";

            이메일인증성공상태로설정(email);

            final var request = getCreateMemberRequest(nickname, email);


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo(ErrorCode.DUPLICATE_NICKNAME.getMessage());
        }
    }


    @Nested
    @DisplayName("회원 로그인")
    public class LoginMember {

        @Test
        @DisplayName("로그인 요청이 정상적으로 처리되어 토큰이 발급되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건

            final String password = "password12";
            final String nickname = "회원로그인성공";
            final String email = "회원로그인성공@email.com";

            이메일인증성공상태로설정(email);
            회원가입처리(nickname, email, password);

            final LoginRequest loginRequest = new LoginRequest(email, password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRequest)
                    .when()
                    .post("/api/auth/login/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            Assertions.assertThat((String) response.jsonPath().get("tokenType")).isNotNull();
            Assertions.assertThat((String) response.jsonPath().get("accessToken")).isNotNull();
            Assertions.assertThat((String) response.jsonPath().get("refreshToken")).isNotNull();
        }


        @Test
        @DisplayName("요청한 이메일로 가입된 회원이 없을 경우 로그인이 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String password = "password12";
            final String nickname = "회원로그인실패1";
            final String email = "회원로그인실패1@email.com";

            이메일인증성공상태로설정(email);
            회원가입처리(nickname, email, password);

            final LoginRequest loginRequest = new LoginRequest("mismatch-email@email.com!!", password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRequest)
                    .when()
                    .post("/api/auth/login/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }


        @Test
        @DisplayName("가입된 회원의 비밀번호와 요청 비밀번호가 불일치시 로그인에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String password = "password12";
            final String nickname = "회원로그인실패2";
            final String email = "회원로그인실패2@email.com";

            이메일인증성공상태로설정(email);
            회원가입처리(nickname, email, password);

            final LoginRequest loginRequest = new LoginRequest(email, "mismatch12!!");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRequest)
                    .when()
                    .post("/api/auth/login/members")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("Password is not match");
        }


        private void 회원가입처리(final String nickname, final String email, final String password) {
            authController.signupMember(new CreateMemberRequest(
                    nickname,
                    email,
                    password,
                    password,
                    "01011112222"
            ));
        }
    }


    @Nested
    @DisplayName("의료진 가입")
    public class SignupStaff {

        @Test
        @DisplayName("요청이 정상적으로 처리되어 가입이 완료되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "의료진가입성공";
            final String email = "의료진가입성공@email.com";
            final String password = "password12";

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진가입성공병원";
            final Long hospitalId = 병원생성(hospitalName);

            final var request = getCreateStaffRequest(nickname, email, password, hospitalId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("이메일 형식이 맞지 않을 경우, 의료진가입에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "의료진가입실패1";
            final String email = "의료진가입실패1";
            final String password = "password12";

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진가입실패1병원";
            final Long hospitalId = 병원생성(hospitalName);

            final var request = getCreateStaffRequest(nickname, email, password, hospitalId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("비밀번호 형식이 적합하지 않을 경우, 의료진가입에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String nickname = "의료진가입실패2";
            final String email = "의료진가입실패2@email.com";
            final String password = "password"; //not include number

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진가입실패2병원";
            final Long hospitalId = 병원생성(hospitalName);

            final var request = getCreateStaffRequest(nickname, email, password, hospitalId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @Test
        @DisplayName("비밀번호와 확인비밀번호가 일치하지 않을 경우, 의료진 가입에 실패한다.")
        public void 실패3() throws Exception {

            //given -- 조건
            final String nickname = "의료진가입실패3";
            final String email = "의료진가입실패3@email.com";
            final String password = "password12";

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진가입실패3병원";
            final Long hospitalId = 병원생성(hospitalName);

            final var request = new CreateStaffRequest(
                    hospitalId,
                    nickname,
                    email,
                    password,
                    "mismatch12!!",
                    "license",
                    "DENTAL"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("Password is not match");
        }

        @Test
        @DisplayName("이메일 인증이 되어있지 않을 경우, 의료진 가입에 실패한다.")
        public void 실패4() throws Exception {

            //given -- 조건
            final String nickname = "의료진가입실패4";
            final String email = "의료진가입실패4@email.com";
            final String password = "password12";


            final String hospitalName = "의료진가입실패4병원";
            final Long hospitalId = 병원생성(hospitalName);

            final var request = getCreateStaffRequest(nickname, email, password, hospitalId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/signup/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }
    }


    @Nested
    @DisplayName("의료진 로그인")
    public class LoginStaff {

        @Test
        @DisplayName("요청이 정상적으로 처리되어 로그인에 성공해야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String password = "password12";
            final String nickname = "의료진로그인성공";
            final String email = "의료진로그인성공@email.com";

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진로그인성공병원";
            final Long hospitalId = 병원생성(hospitalName);

            의료진가입처리(password, nickname, email, hospitalId);

            final var request = new LoginRequest(email, password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/login/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            Assertions.assertThat((String) response.jsonPath().get("tokenType")).isNotNull();
            Assertions.assertThat((String) response.jsonPath().get("accessToken")).isNotNull();
            Assertions.assertThat((String) response.jsonPath().get("refreshToken")).isNotNull();
        }


        @Test
        @DisplayName("이메일 형식이 맞지 않을 경우, 로그인에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String password = "password12";
            final String nickname = "의료진로그인실패1";
            final String email = "의료진로그인실패1";

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진로그인실패1병원";
            final Long hospitalId = 병원생성(hospitalName);

            의료진가입처리(password, nickname, email, hospitalId);

            final var request = new LoginRequest(email, password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/login/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("error")).isEqualTo("Bad Request");
        }


        @Test
        @DisplayName("비밀번호 유효성 검사에 적합하지 않을 경우, 로그인에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String password = "password";
            final String nickname = "의료진로그인실패2";
            final String email = "의료진로그인실패2@email.com";

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진로그인실패2병원";
            final Long hospitalId = 병원생성(hospitalName);

            의료진가입처리(password, nickname, email, hospitalId);

            final var request = new LoginRequest(email, password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/login/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("error")).isEqualTo("Bad Request");
        }


        @Test
        @DisplayName("해당 이메일로 가입된 의료진이 존재하지 않을 경우, 로그인에 실패한다.")
        public void 실패3() throws Exception {

            //given -- 조건
            final String password = "password12";
            final String email = "의료진로그인실패3@email.com";

            final var request = new LoginRequest(email, password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/login/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }

        
        @Test
        @DisplayName("비밀번호가 일치하지 않을 경우, 로그인에 실패한다.")
        public void 실패4() throws Exception {

            //given -- 조건
            final String password = "password12";
            final String nickname = "의료진로그인실패4";
            final String email = "의료진로그인실패4@email.com";

            이메일인증성공상태로설정(email);

            final String hospitalName = "의료진로그인실패4병원";
            final Long hospitalId = 병원생성(hospitalName);

            의료진가입처리(password, nickname, email, hospitalId);

            final var request = new LoginRequest(email, "mismatch12!!!");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/api/auth/login/staff")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("Password is not match");
        }
        

        private CreateStaffResponse 의료진가입처리(final String password, final String nickname, final String email, final Long hospitalId) {
            return authController.signupStaff(new CreateStaffRequest(
                    hospitalId,
                    nickname,
                    email,
                    password,
                    password,
                    "license",
                    "DENTAL"
            ));
        }
    }


    @NotNull
    private static CreateStaffRequest getCreateStaffRequest(String nickname, String email, String password, Long hospitalId) {
        return new CreateStaffRequest(
                hospitalId,
                nickname,
                email,
                password,
                password,
                "license",
                "DENTAL"
        );
    }

    private Long 병원생성(String hospitalName) {
        return hospitalRepository.save(Hospital.createHospital(
                hospitalName,
                "number",
                "address",
                "oper"
        )).getId();
    }


    private void 이메일인증성공상태로설정(final String email) {
        mailAuthenticationRepository.save(
                DBMailAuthentication.builder()
                        .email(email)
                        .isVerified(true)
                        .build()
        );
    }

    @NotNull
    private static CreateMemberRequest getCreateMemberRequest(final String nickname, final String email) {
        return new CreateMemberRequest(
                nickname,
                email,
                "password12",
                "password12",
                "phonenumber"
        );
    }
}
