package io.wisoft.capstonedesign.domain.auth.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
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

class JwtReIssueControllerTest extends ApiTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("엑세스토큰 재발급")
    public class ReIssue {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 새로운 액세스 토큰이 발급되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String email = "재발급성공@email.com";
            final String refreshToken = jwtTokenProvider.createRefreshToken(email);

            memberRepository.save(Member.newInstance(
                    "재발급성공",
                    email,
                    "password12",
                    "phoneNumber"
            ));

            redisAdapter.setValue(email, refreshToken, 3600000, TimeUnit.SECONDS);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + refreshToken)
                    .when()
                    .get("/jwt/re-issuance")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            Assertions.assertThat((String) response.jsonPath().get("accessToken")).isNotNull();
            Assertions.assertThat((String) response.jsonPath().get("refreshToken")).isNotNull();
        }


        @Test
        @DisplayName("유효하지 않은 리프레시 토큰으로 요청할 경우, 재발급에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String email = "재발급실패1@email.com";
            final String refreshToken = jwtTokenProvider.createRefreshToken(email);

            redisAdapter.setValue(email, refreshToken, 3600000, TimeUnit.SECONDS);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + "invalid-refreshtoken")
                    .when()
                    .get("/jwt/re-issuance")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("JWT is invalid");
        }


        @Test
        @DisplayName("헤더에 리프레시토큰이 적재되지 않았을 경우, 재발급에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String email = "재발급실패2@email.com";
            final String refreshToken = jwtTokenProvider.createRefreshToken(email);

            redisAdapter.setValue(email, refreshToken, 3600000, TimeUnit.SECONDS);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/jwt/re-issuance")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }
}