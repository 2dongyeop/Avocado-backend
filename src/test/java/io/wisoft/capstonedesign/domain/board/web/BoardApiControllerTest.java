package io.wisoft.capstonedesign.domain.board.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.domain.board.web.dto.*;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


public class BoardApiControllerTest extends ApiTest {

    @Test
    public void createBoard_success() throws Exception {

        final var request = getCreateBoardRequest();
        final var response = createBoard(request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void updateBoard() throws Exception {

        final int targetBoardId = 1;
        final UpdateBoardRequest request = getUpdateBoardRequest();

        final var response = updateBoard(targetBoardId, request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> updateBoard(final int targetBoardId, final UpdateBoardRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/api/boards/" + targetBoardId)
                .then()
                .log().all().extract();
    }


    @Test
    public void deleteBoard() throws Exception {

        final int targetBoardId = 1;

        final var response = deleteBoard(targetBoardId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> deleteBoard(int targetBoardId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/boards/" + targetBoardId)
                .then()
                .log().all().extract();
    }


    private UpdateBoardRequest getUpdateBoardRequest() {
        return new UpdateBoardRequest(
                "newTitle",
                "newBody"
        );
    }

    private CreateBoardRequest getCreateBoardRequest() {
        return new CreateBoardRequest(
                1L,
                "title",
                "body",
                "DENTAL",
                "boardPhotoPath"
        );
    }

    private ExtractableResponse<Response> createBoard(final CreateBoardRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/boards")
                .then()
                .log().all().extract();
    }
}