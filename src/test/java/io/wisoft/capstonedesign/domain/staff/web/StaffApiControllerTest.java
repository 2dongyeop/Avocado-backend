package io.wisoft.capstonedesign.domain.staff.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.setting.api.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

class StaffApiControllerTest extends ApiTest {

    @Test
    public void updateStaff() throws Exception {

        final int targetStaffId = 1;
        final String photoPath = "newPhotoPath";

        final var response = updateStaff(targetStaffId, photoPath);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void deleteStaff() throws Exception {

        final int targetStaffId = 1;

        final var response = deleteStaff(targetStaffId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> deleteStaff(int targetStaffId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/staff/" + targetStaffId)
                .then()
                .log().all().extract();
    }

    private static ExtractableResponse<Response> updateStaff(final int targetStaffId, final String photoPath) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/api/staff/" + targetStaffId + "?photoPath=" + photoPath)
                .then()
                .log().all().extract();
    }
}