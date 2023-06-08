package io.wisoft.capstonedesign.domain.member.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberApiControllerTest extends ApiTest {

    @Test
    public void readMember() throws Exception {

        final int targetMemberId = 2;

        final var response = readMember(targetMemberId);

        System.out.println(response.body());
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void updateMember() throws Exception {

        final int targetMemberId = 1;
        final String nickname = "hello";

        final var response = updateMember(targetMemberId, nickname);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void deleteMember() throws Exception {

        final int targetMemberId = 1;

        final var response = deleteMember(targetMemberId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> deleteMember(final int targetMemberId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/members/" + targetMemberId)
                .then()
                .log().all().extract();
    }

    private static ExtractableResponse<Response> updateMember(final int targetMemberId, final String nickname) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/api/members/" + targetMemberId + "?nickname=" + nickname)
                .then()
                .log().all().extract();
    }

    private static ExtractableResponse<Response> readMember(final int targetMemberId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/members/" + targetMemberId + "/details")
                .then()
                .log().all().extract();
    }
}