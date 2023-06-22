package io.wisoft.capstonedesign.domain.appointment.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.appointment.persistence.AppointmentRepository;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentRequest;
import io.wisoft.capstonedesign.domain.appointment.web.dto.UpdateAppointmentRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.PayStatus;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import io.wisoft.capstonedesign.global.redis.RedisAdapter;
import io.wisoft.capstonedesign.setting.common.ApiTest;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

class AppointmentControllerTest extends ApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;


    @Nested
    @DisplayName("예약 저장")
    class CreateAppointment {
        @Test
        @DisplayName("예약 저장 요청시 DB에 정상적으로 저장이 돼야 한다.")
        public void 성공() throws Exception {

            //given
            final String email = "예약저장성공";
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final var request = 저장요청생성(
                    getMember("예약저장성공@email", "예약저장성공").getId(),
                    getHospital("예약저장성공").getId());

            //when
            final var response = RestAssured
                    .given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(request)
                    .when()
                        .post("/api/appointments")
                    .then()
                        .log().all().extract();
            //then
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("예약 날짜가 현재 날짜보다 이전일 경우 예외가 발생해야 한다.")
        public void 실패1() throws Exception {
            //given -- 조건
            final String email = "예약저장실패1@email.com";
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final var invalidRequest = new CreateAppointmentRequest(
                    getMember("예약저장실패1@naver.com", "예약저장실패1").getId(),
                    getHospital("예약저장실패1").getId(),
                    "DENTAL",
                    "예약코멘트",
                    "이동엽",
                    "01012341234",
                    LocalDateTime.now().minusDays(1)
            );

            //when
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(invalidRequest)
                    .when()
                    .post("/api/appointments")
                    .then()
                    .log().all().extract();

            //then
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("예약 삭제")
    class DeleteAppointment {

        @Test
        @DisplayName("예약 삭제 요청시 DB에서 삭제되어야 한다.")
        public void 성공() throws Exception {

            //given
            final String hospitalName = "예약삭제성공";
            final String memberNickname = "예약삭제성공";
            final String email = "예약삭제성공@naver.com";

            //JWT setting
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            //member & hospital & appointment save
            final Long id = getAppointment(email, memberNickname, hospitalName).getId();

            //when
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken)
                    .when()
                    .delete("/api/appointments/" + id)
                    .then()
                    .log().all().extract();

            //then
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 예약을 삭제할려고 하면 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String email = "예약삭제실패1@naver.com";
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken)
                    .when()
                    .delete("/api/appointments/" + 10000)
                    .then()
                    .log().all().extract();


            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("예약수정")
    public class UpdateAppointment {

        @Test
        @DisplayName("예약 수정 요청시 예약 정보가 수정되어야 한다.")
        public void 성공() throws Exception {

            //given
            final String hospitalName = "예약수정성공";
            final String memberNickname = "예약수정성공";
            final String email = "예약수정성공@naver.com";

            //JWT setting
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            //member & hospital & appointment save
            final Long id = getAppointment(email, memberNickname, hospitalName).getId();

            final UpdateAppointmentRequest request = 수정요청생성();

            //when
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(request)
                    .when()
                    .patch("/api/appointments/" + id)
                    .then()
                    .log().all().extract();

            //then
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("예약 수정시 존재하지 않는 병과를 예약하도록 수정하면 오류가 발생한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String hospitalName = "예약수정실패1";
            final String memberNickname = "예약수정실패1";
            final String email = "예약수정실패1@naver.com";

            //JWT setting
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            //member & hospital & appointment save
            final Long id = getAppointment(email, memberNickname, hospitalName).getId();

            final UpdateAppointmentRequest badRequest = new UpdateAppointmentRequest(
                    "non-exist-dept",
                    "comment",
                    "updateName",
                    "updateNumber"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(badRequest)
                    .when()
                    .patch("/api/appointments/" + id)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        private UpdateAppointmentRequest 수정요청생성() {
            return new UpdateAppointmentRequest(
                    "DENTAL",
                    "comment",
                    "updateName",
                    "updateNumber"
            );
        }
    }

    private CreateAppointmentRequest 저장요청생성(
            final Long memberId,
            final Long hospitalId
    ) {
        return new CreateAppointmentRequest(
                memberId,
                hospitalId,
                "DENTAL",
                "예약코멘트",
                "이동엽",
                "01012341234",
                LocalDateTime.now().plusMonths(1)
        );
    }

    @NotNull
    private Appointment getAppointment(final String email, final String memberNickname, final String hospitalName) {
        return appointmentRepository.save(Appointment.createAppointment(
                getMember(email, memberNickname),
                getHospital(hospitalName),
                HospitalDept.DENTAL,
                "comment",
                "appointName",
                "appointPhone",
                PayStatus.NONE,
                LocalDateTime.now().plusMonths(1)
        ));
    }

    @NotNull
    private Hospital getHospital(final String hospitalName) {
        return hospitalRepository.save(
                Hospital.createHospital(
                        hospitalName,
                        "number",
                        "address",
                        "oper"));
    }

    @NotNull
    private Member getMember(final String email, final String memberNickname) {
        return memberRepository.save(
                Member.newInstance(
                        memberNickname,
                        email,
                        "pass123",
                        "phone"));
    }
}
