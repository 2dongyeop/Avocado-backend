package io.wisoft.capstonedesign.domain.reviewreply.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.CreateReviewReplyRequest;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.UpdateReviewReplyRequest;
import io.wisoft.capstonedesign.setting.api.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

class ReviewReplyApiControllerTest extends ApiTest {

    @Test
    public void createReviewReply() throws Exception {

        final CreateReviewReplyRequest request = getCreateReviewReplyRequest();

        final var response = createReviewReply(request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void deleteReviewReply() throws Exception {

        final int targetReviewReplyId = 1;

        final var response = deleteReviewReply(targetReviewReplyId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void updateReviewReply() throws Exception {

        final int targetReviewReplyId = 1;
        final UpdateReviewReplyRequest request = new UpdateReviewReplyRequest("new reply");

        final var response = updateReviewReply(targetReviewReplyId, request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> updateReviewReply(final int targetReviewReplyId, final UpdateReviewReplyRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/api/review-reply/" + targetReviewReplyId)
                .then()
                .log().all().extract();
    }


    private static ExtractableResponse<Response> deleteReviewReply(final int targetReviewReplyId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/review-reply/" + targetReviewReplyId)
                .then()
                .log().all().extract();
    }


    private static ExtractableResponse<Response> createReviewReply(final CreateReviewReplyRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/review-reply")
                .then()
                .log().all().extract();
    }

    private static CreateReviewReplyRequest getCreateReviewReplyRequest() {
        return new CreateReviewReplyRequest(
                1L,
                1L,
                "reply"
        );
    }
}