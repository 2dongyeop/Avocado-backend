package io.wisoft.capstonedesign.domain.pick.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.persistence.PickRepository;
import io.wisoft.capstonedesign.domain.pick.web.dto.CreatePickRequest;
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

import java.util.concurrent.TimeUnit;

class PickApiControllerTest extends ApiTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;

    @Autowired
    private PickRepository pickRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Nested
    @DisplayName("찜하기 생성")
    public class CreatePick {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 찜하기에 성공해야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "찜하기생성성공";
            final String email = "찜하기생성성공@naver.com";
            final String password = "password12";

            final String hospitalName = "찜하기생성성공병원";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);

            final var request = new CreatePickRequest(member.getId(), hospital.getId());

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .post("/api/picks")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("잘못된 회원 정보를 입력할 경우, 찜하기에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String email = "찜하기생성실패1@naver.com";

            final String hospitalName = "찜하기생성실패1병원";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Hospital hospital = 병원생성(hospitalName);

            final var request = new CreatePickRequest(2020L, hospital.getId());

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .post("/api/picks")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @Test
        @DisplayName("잘못된 병원 정보를 입력할 경우, 찜하기에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String nickname = "찜하기생성실패2";
            final String email = "찜하기생성실패2@naver.com";
            final String password = "password12";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);

            final var request = new CreatePickRequest(member.getId(), 2202L);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .post("/api/picks")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("찜하기 취소")
    public class deletePick {

        @Test
        @DisplayName("요청이 정상적으로 수행되어, 찜하기가 취소된다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "찜하기취소성공";
            final String email = "찜하기취소성공@naver.com";
            final String password = "password12";
            final String hospitalName = "찜하기생성취소병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);

            final Pick pick = 찜하기생성(member, hospital);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/picks/{id}", pick.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 찜하기를 취소하려 할 경우, 취소에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "찜하기취소실패1";
            final String email = "찜하기취소실패1@naver.com";
            final String password = "password12";
            final String hospitalName = "찜하기실패1병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/picks/{id}", 12423L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    private Pick 찜하기생성(final Member member, final Hospital hospital) {
        return pickRepository.save(Pick.createPick(
                member,
                hospital
        ));
    }


    @NotNull
    private Member 회원생성(final String nickname, final String email, final String password) {
        return memberRepository.save(
                Member.newInstance(
                        nickname,
                        email,
                        password,
                        "phone")
        );
    }


    @NotNull
    private Hospital 병원생성(final String hospitalName) {
        return hospitalRepository.save(Hospital.createHospital(
                hospitalName,
                "number",
                "address",
                "oper"
        ));
    }
}