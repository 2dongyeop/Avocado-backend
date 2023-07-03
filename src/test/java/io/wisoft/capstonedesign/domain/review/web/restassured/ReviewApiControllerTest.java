package io.wisoft.capstonedesign.domain.review.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.review.persistence.ReviewRepository;
import io.wisoft.capstonedesign.domain.review.web.dto.CreateReviewRequest;
import io.wisoft.capstonedesign.domain.review.web.dto.UpdateReviewRequest;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
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

class ReviewApiControllerTest extends ApiTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;

    @Nested
    @DisplayName("리뷰 저장")
    public class CreateReview {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 리뷰가 저장되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "리뷰저장성공";
            final String email = "리뷰저장성공@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰저장성공병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            병원생성(hospitalName);

            final var request = getCreateReviewRequest(hospitalName, member);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .post("/api/reviews")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("리뷰의 별점이 0점에서~5점 사이가 아닐 경우, 리뷰 저장에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "리뷰저장실패1";
            final String email = "리뷰저장실패1@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰저장실패1병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            병원생성(hospitalName);

            final var request = new CreateReviewRequest(
                    member.getId(),
                    "title",
                    "body",
                    6,
                    hospitalName,
                    "DEANTAL",
                    "photopath"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .post("/api/reviews")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @Test
        @DisplayName("잘못된 회원 정보와 함께 리뷰를 저장하려 할 경우, 저장에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String email = "리뷰저장실패2@naver.com";
            final String hospitalName = "리뷰저장실패2병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            병원생성(hospitalName);

            final var request = new CreateReviewRequest(
                    1000L,
                    "title",
                    "body",
                    6,
                    hospitalName,
                    "DEANTAL",
                    "photopath"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .post("/api/reviews")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @NotNull
        private CreateReviewRequest getCreateReviewRequest(final String hospitalName, final Member member) {
            return new CreateReviewRequest(
                    member.getId(),
                    "title",
                    "body",
                    5,
                    hospitalName,
                    "DENTAL",
                    "photopath"
            );
        }
    }


    @Nested
    @DisplayName("리뷰 수정")
    public class UpdateReview {

        @Test
        @DisplayName("요청이 성공적으로 처리되어, 리뷰가 수정되어야 한다. ")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "리뷰수정성공";
            final String email = "리뷰수정성공@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰수정성공병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);

            final var request = new UpdateReviewRequest(
                    "newTitle",
                    "newBody"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/reviews/{id}", review.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            Assertions.assertThat((String) response.jsonPath().get("newTitle")).isEqualTo("newTitle");
            Assertions.assertThat((String) response.jsonPath().get("newBody")).isEqualTo("newBody");
        }


        @Test
        @DisplayName("존재하지 않는 리뷰를 수정하려 할 경우, 수정에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "리뷰수정실패1";
            final String email = "리뷰수정실패1@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰수정실패1병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            병원생성(hospitalName);

            final var request = new UpdateReviewRequest(
                    "newTitle",
                    "newBody"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/reviews/{id}", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("리뷰 삭제")
    public class DeleteReview {

        @Test
        @DisplayName("요청이 정상적으로 동작하여, 리뷰가 삭제되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "리뷰삭제성공";
            final String email = "리뷰삭제성공@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰삭제성공병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/reviews/{id}", review.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            Assertions.assertThat((String) response.jsonPath().get("status")).isEqualTo("DELETE");
        }

        @Test
        @DisplayName("존재하지 않는 리뷰를 삭제하려 할 경우, 삭제에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "리뷰삭제실패1";
            final String email = "리뷰삭제실패1@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰삭제실패1병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            병원생성(hospitalName);


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/reviews/{id}", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    private Review 리뷰생성(final String hospitalName, final Member member) {
        return reviewRepository.save(Review.createReview(
                member,
                "title",
                "body",
                "photo",
                1,
                hospitalName,
                HospitalDept.DENTAL
        ));
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
}