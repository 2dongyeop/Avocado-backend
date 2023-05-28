package io.wisoft.capstonedesign.domain.pick.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.domain.pick.web.dto.CreatePickRequest;
import io.wisoft.capstonedesign.setting.api.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class PickApiControllerTest extends ApiTest {

    @Test
    public void createPick() throws Exception {

        final var request = getCreatePickRequest();

        final var response = createPick(request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    public void deletePick() throws Exception {

        final int targetPickId = 1;

        final var response = deletePick(targetPickId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void readPick() throws Exception {

        final int targetPickId = 1;

        final var response = readPick(targetPickId);

        System.out.println(response.body());
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> readPick(final int targetPickId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/picks/" + targetPickId + "/details")
                .then()
                .log().all().extract();
    }


    private static ExtractableResponse<Response> deletePick(final int targetPickId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/picks/" + targetPickId)
                .then()
                .log().all().extract();
    }

    private static ExtractableResponse<Response> createPick(final CreatePickRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/picks")
                .then()
                .log().all().extract();
    }

    private static CreatePickRequest getCreatePickRequest() {
        return new CreatePickRequest(1L, 1L);
    }
}