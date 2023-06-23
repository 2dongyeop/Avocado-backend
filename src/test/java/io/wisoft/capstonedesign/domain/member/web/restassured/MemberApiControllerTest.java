package io.wisoft.capstonedesign.domain.member.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberPasswordRequest;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.concurrent.TimeUnit;

class MemberApiControllerTest extends ApiTest {


    @Autowired
    private RedisAdapter redisAdapter;

    @Autowired
    private EncryptHelper encryptHelper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Nested
    @DisplayName("회원 비밀번호 변경")
    public class UpdateMemberPassword {


        @Test
        @DisplayName("요청이 성공적으로 처리되어, 회원의 비밀번호가 바뀌어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "passChangeSuccess";
            final String email = "passChangeSuccess@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final var request = new UpdateMemberPasswordRequest(password,"newPassword12");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/members/{id}/password", memberId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 회원 정보를 입력할 경우, 비밀번호 변경에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "passChangeFail1";
            final String email = "passChangeFail1@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final var request = new UpdateMemberPasswordRequest(password,"newPassword12");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/members/{id}/password", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @Test
        @DisplayName("기존 비밀번호가 일치하지 않을 경우, 비밀번호 변경에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String nickname = "passChangeFail2";
            final String email = "passChangeFail2@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final var request = new UpdateMemberPasswordRequest("failPassword12","newPassword12");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/members/{id}/password", memberId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("회원 정보 수정")
    public class UpdateMember {

        @Test
        @DisplayName("요청이 성공적으로 수행될 경우, 사진이 수정된다.")
        public void 사진업데이트성공() throws Exception {

            //given -- 조건
            final String nickname = "photoUpdateSuccess1";
            final String email = "photoUpdateSuccess1@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final String newPhotoPath = "newPhotoPath";

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/members/{id}?photoPath=" + newPhotoPath, memberId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("요청이 성공적으로 수행될 경우, 닉네임이 수정된다.")
        public void 닉네임업데이트성공() throws Exception {

            //given -- 조건
            final String nickname = "photoUpdateSuccess2";
            final String email = "photoUpdateSuccess2@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final String newNickname = "newNickname";

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/members/{id}?nickname=" + newNickname, memberId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }
    }


    @Nested
    @DisplayName("회원 탈퇴")
    public class DeleteMember {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 회원이 삭제되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "회원탈퇴성공";
            final String email = "회원탈퇴성공@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/members/{id}", memberId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 회원 정보를 입력할 경우, 회원 탈퇴에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "회원탈퇴실패1";
            final String email = "회원탈퇴실패1@naver.com";
            final String password = "password12";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/members/{id}", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    private Long 회원생성(final String nickname, final String email, final String password) {
        return memberRepository.save(Member.newInstance(
                nickname,
                email,
                encryptHelper.encrypt(password),
                "phonenumber"
        )).getId();
    }
}