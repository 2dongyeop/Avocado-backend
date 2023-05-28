package io.wisoft.capstonedesign.domain.review.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.domain.review.web.dto.CreateReviewRequest;
import io.wisoft.capstonedesign.domain.review.web.dto.UpdateReviewRequest;
import io.wisoft.capstonedesign.setting.api.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ReviewApiControllerTest extends ApiTest {

    @Test
    public void readReview() throws Exception {

        final int targetReviewId = 1;

        final var response = readReview(targetReviewId);

        System.out.println(response.body());
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void createReview() throws Exception {

        final CreateReviewRequest request = getCreateReviewRequest();

        final var response = createReview(request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    
    
    @Test
    public void updateReview() throws Exception {
        
        final int targetReviewId = 1;
        final UpdateReviewRequest request = getUpdateReviewRequest();

        final var response = updateReview(targetReviewId, request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void deleteReview() throws Exception {

        final int targetReviewId = 1;

        final var response = deleteReview(targetReviewId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> deleteReview(final int targetReviewId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/reviews/" + targetReviewId)
                .then()
                .log().all().extract();
    }


    private static ExtractableResponse<Response> updateReview(final int targetReviewId, final UpdateReviewRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/api/reviews/" + targetReviewId)
                .then()
                .log().all().extract();
    }

    private static UpdateReviewRequest getUpdateReviewRequest() {
        return new UpdateReviewRequest("new Title", "new Body");
    }

    private static CreateReviewRequest getCreateReviewRequest() {
        return new CreateReviewRequest(
                1L,
                "title",
                "body",
                1,
                "서울대병원",
                "photoPath"
        );
    }

    private static ExtractableResponse<Response> createReview(final CreateReviewRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/reviews")
                .then()
                .log().all().extract();
    }

    private static ExtractableResponse<Response> readReview(final int targetReviewId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/reviews/" + targetReviewId + "/details")
                .then()
                .log().all().extract();
    }

}