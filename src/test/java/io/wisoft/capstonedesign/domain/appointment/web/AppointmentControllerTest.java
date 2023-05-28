package io.wisoft.capstonedesign.domain.appointment.web;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentRequest;
import io.wisoft.capstonedesign.domain.appointment.web.dto.UpdateAppointmentRequest;
import io.wisoft.capstonedesign.setting.api.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class AppointmentControllerTest extends ApiTest {

    @Test
    public void 예약저장() throws Exception {

        final var request = 저장요청생성();

        final var response = createAppointment(request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> createAppointment(final CreateAppointmentRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post("/api/appointments")
                .then()
                .log().all().extract();
    }


    @Test
    public void 예약삭제() throws Exception {

        final int deleteAppointmentId = 1;

        final var response = deleteAppointment(deleteAppointmentId);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> deleteAppointment(final int deleteAppointmentId) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/appointments/" + deleteAppointmentId)
                .then()
                .log().all().extract();
    }


    @Test
    public void 예약수정() throws Exception {

        final int updateAppointmentId = 1;

        final UpdateAppointmentRequest request = 수정요청생성();

        final var response = updateAppointment(updateAppointmentId, request);

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> updateAppointment(final int updateAppointmentId, final UpdateAppointmentRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .patch("/api/appointments/" + updateAppointmentId)
                .then()
                .log().all().extract();
    }


    private static UpdateAppointmentRequest 수정요청생성() {
        return new UpdateAppointmentRequest(
                "DENTAL",
                "comment",
                "updateName",
                "updateNumber"
        );
    }


    private static CreateAppointmentRequest 저장요청생성() {
        return new CreateAppointmentRequest(
                1L,
                1L,
                "DENTAL",
                "예약코멘트",
                "이동엽",
                "01012341234"
        );
    }
}
