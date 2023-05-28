package io.wisoft.capstonedesign.domain.member.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.setting.api.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class MemberMyPageControllerTest extends ApiTest {

    @Test
    public void reviewsByMemberId() throws Exception {

        final int targetMemberId = 1;

        final var response = readMemberByMyPage(targetMemberId, "/my-page/reviews");

        System.out.println(response.body());
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void boardsByMemberId() throws Exception {

        final int targetMemberId = 1;

        final var response = readMemberByMyPage(targetMemberId, "/my-page/boards");

        System.out.println(response.body());
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void appointmentsByMemberId() throws Exception {

        final int targetMemberId = 1;

        final var response = readMemberByMyPage(targetMemberId, "/my-page/appointments");

        System.out.println(response.body());
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void picksByMemberId() throws Exception {

        final int targetMemberId = 1;

        final var response = readMemberByMyPage(targetMemberId, "/my-page/picks");

        System.out.println(response.body());
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> readMemberByMyPage(final int targetMemberId, final String url) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/members/" + targetMemberId + url)
                .then()
                .log().all().extract();
    }
}