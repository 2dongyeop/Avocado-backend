package io.wisoft.capstonedesign.domain.payment.web;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.concurrent.TimeUnit;

class RefundControllerTest extends ApiTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;


    @Test
    public void getToken_success() throws Exception {

        //given
        final String email = "getTokenSuccess@naver.com";

        //JWT
        final String accessToken = jwtTokenProvider.createAccessToken(email);
        redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

        //when
        final var response = RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "bearer " + accessToken)
                .when()
                .get("/payment/token")
                .then()
                .log().all().extract();

        //then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}