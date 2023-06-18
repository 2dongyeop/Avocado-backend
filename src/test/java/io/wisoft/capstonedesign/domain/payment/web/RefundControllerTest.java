package io.wisoft.capstonedesign.domain.payment.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.persistence.AppointmentRepository;
import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RefundControllerTest extends ApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    public void getToken_success() throws Exception {

        final var response = getToken();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void refund_success() throws Exception {

        //given -- 조건
        final Long appointmentId = 1L;
        final Map<String, String> map = new HashMap<>();
        map.put("merchantUid", "1");

        final String jsonBody = objectMapper.writeValueAsString(map);

        //when -- 동작
        mockMvc.perform(post("/payment/cancel/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andDo(print());

        //then -- 검증
        final Appointment find = appointmentRepository.findById(appointmentId).orElseThrow();
        Assertions.assertThat(find.getPayStatus()).isEqualTo(PayStatus.REFUND);
    }


    private ExtractableResponse<Response> getToken() {
        final var response = RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/payment/token")
                .then()
                .log().all().extract();
        return response;
    }
}