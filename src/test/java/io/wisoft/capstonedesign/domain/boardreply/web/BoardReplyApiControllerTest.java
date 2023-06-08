package io.wisoft.capstonedesign.domain.boardreply.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.CreateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.UpdateBoardReplyRequest;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


public class BoardReplyApiControllerTest extends ApiTest {

    @Test
    public void createBoardReply_success() throws Exception {

        final CreateBoardReplyRequest request = getCreateBoardReplyRequest();

        final var response = createBoardReply(request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void updateBoardReply() throws Exception {

        final int targetBoardReplyId = 2;
        final UpdateBoardReplyRequest request = getUpdateBoardReplyRequest();

        final var response = updateBoardReply(targetBoardReplyId, request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void deleteBoardReply() throws Exception {

        final int targetBoardReplyId = 1;

        final var response = deleteBoardReply(targetBoardReplyId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void readBoardReply() throws Exception {

        final int targetBoardReplyId = 1;
        final var response = readBoardReply(targetBoardReplyId);

        String string = response.body().toString();
        System.out.println("string = " + string);
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    private ExtractableResponse<Response> readBoardReply(int targetBoardReplyId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/board-reply/" + targetBoardReplyId + "/details")
                .then()
                .log().all().extract();
    }


    private ExtractableResponse<Response> deleteBoardReply(int targetBoardReplyId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/board-reply/" + targetBoardReplyId)
                .then()
                .log().all().extract();
    }

    private ExtractableResponse<Response> updateBoardReply(final int targetBoardReplyId, final UpdateBoardReplyRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/api/board-reply/" + targetBoardReplyId)
                .then()
                .log().all().extract();
    }

    private UpdateBoardReplyRequest getUpdateBoardReplyRequest() {
        return new UpdateBoardReplyRequest(
                "update-reply"
        );
    }

    private ExtractableResponse<Response> createBoardReply(final CreateBoardReplyRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/board-reply")
                .then()
                .log().all().extract();
    }

    private CreateBoardReplyRequest getCreateBoardReplyRequest() {
        return new CreateBoardReplyRequest(
                1L,
                1L,
                "reply"
        );
    }
}