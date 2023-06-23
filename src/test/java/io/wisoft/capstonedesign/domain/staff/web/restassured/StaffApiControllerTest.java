package io.wisoft.capstonedesign.domain.staff.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.concurrent.TimeUnit;

class StaffApiControllerTest extends ApiTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;

    @Autowired
    private EncryptHelper encryptHelper;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private HospitalRepository hospitalRepository;


    @Nested
    @DisplayName("의료진 비밀번호 변경")
    public class UpdateStaffPassword {

        @Test
        @DisplayName("요청이 성공적으로 처리되어, 비밀번호가 변경되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진비번변경성공병원";
            final String nickname = "의료진비번변경성공";
            final String email = "의료진비번변경성공@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            final var request = new UpdateStaffPasswordRequest(password, "newPass12");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/staff/{id}/password", staff.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 회원의 비밀번호를 변경하려 할 경우, 실패한다")
        public void 실패1() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진비번변경실패1병원";
            final String nickname = "의료진비번변경실패1";
            final String email = "의료진비번변경실패1@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            final var request = new UpdateStaffPasswordRequest(password, "newPass12");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/staff/{id}/password", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @Test
        @DisplayName("입력한 기존 비밀번호가 일치하지 않을 경우, 비밀번호 변경에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진비번변경실패2병원";
            final String nickname = "의료진비번변경실패2";
            final String email = "의료진비번변경실패2@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            final var request = new UpdateStaffPasswordRequest("mismatch12", "newPass12");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/staff/{id}/password", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("의료진 정보 수정")
    public class UpdateStaff {

        @Test
        @DisplayName("요청이 성공적으로 처리되어, 의료진 사진이 수정되어야 한다.")
        public void 성공1() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진정보수정성공1병원";
            final String nickname = "의료진정보수정성공1";
            final String email = "의료진정보수정성공1@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            final String newPhotoPath = "newPhotoPath";

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/staff/{id}?photoPath=" + newPhotoPath, staff.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("요청이 성공적으로 처리되어, 의료진 소속 병원이 수정되어야 한다.")
        public void 성공2() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진정보수정성공2병원";
            final String nickname = "의료진정보수정성공2";
            final String email = "의료진정보수정성공2@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Hospital hospital2 = 병원생성(hospitalName + "2");
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/staff/{id}?hospitalName=" + hospitalName + "2", staff.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 병원으로 의료진 병원을 수정할 시, 수정에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진정보수정실패1병원";
            final String nickname = "의료진정보수정실패1";
            final String email = "의료진정보수정실패1@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/staff/{id}?hospitalName=" + hospitalName + "2", staff.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("의료진 탈퇴")
    public class DeleteStaff {

        @Test
        @DisplayName("요청이 성공적으로 처리되어, 의료진 탈퇴에 성공한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진삭제성공병원";
            final String nickname = "의료진삭제성공";
            final String email = "의료진삭제성공@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/staff/{id}", staff.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 의료진을 삭제하려 할 경우, 요청이 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String hospitalName = "의료진삭제실패1병원";
            final String nickname = "의료진삭제실패1";
            final String email = "의료진삭제실패1@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/staff/{id}", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @NotNull
    private Staff 의료진생성(final String nickname, final String email, final String password, final Hospital hospital) {
        return staffRepository.save(Staff.newInstance(
                hospital,
                nickname,
                email,
                encryptHelper.encrypt(password),
                "licensse",
                HospitalDept.DENTAL
        ));
    }


    @NotNull
    private Hospital 병원생성(final String hospitalName) {
        return hospitalRepository.save(Hospital.createHospital(
                hospitalName,
                "number",
                "address",
                "oper"
        ));
    }
}