package io.wisoft.capstonedesign.domain.reviewreply.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.review.persistence.ReviewRepository;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReplyRepository;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.CreateReviewReplyRequest;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.UpdateReviewReplyRequest;
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

class ReviewReplyApiControllerTest extends ApiTest {

    @Autowired
    private RedisAdapter redisAdapter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private ReviewReplyRepository reviewReplyRepository;


    @Nested
    @DisplayName("리뷰댓글 작성")
    public class CreateReviewReply {

        @Test
        @DisplayName("요청이 성공적으로 처리되어, 리뷰댓글이 작성되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "리뷰댓글작성성공";
            final String email = "리뷰댓글작성성공@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰댓글작성성공병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);

            final var request = new CreateReviewReplyRequest(
                    member.getId(),
                    review.getId(),
                    "reply"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .post("/api/review-reply")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("올바르지 않은 회원 정보와 함께 리뷰댓글을 작성하려 할 경우, 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "리뷰댓글작성실패1";
            final String email = "리뷰댓글작성실패1@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰댓글작성실패1병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);

            final var request = new CreateReviewReplyRequest(
                    1000L,
                    review.getId(),
                    "reply"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .post("/api/review-reply")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @Test
        @DisplayName("존재하지 않는 리뷰에 댓글을 작성하려 할 경우, 실패한다")
        public void 실패2() throws Exception {

            //given -- 조건
            final String nickname = "리뷰댓글작성실패2";
            final String email = "리뷰댓글작성실패2@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰댓글작성실패2병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);

            final var request = new CreateReviewReplyRequest(
                    member.getId(),
                    1000L,
                    "reply"
            );

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .post("/api/review-reply")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("리뷰댓글 삭제")
    public class DeleteReviewReply {

        @Test
        @DisplayName("요청이 성공적으로 처리되어, 리뷰댓글이 삭제되어야한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "리뷰댓글삭제성공";
            final String email = "리뷰댓글삭제성공@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰댓글작성삭제병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);
            final ReviewReply reviewReply = 리뷰댓글생성(member, review);


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/review-reply/{id}", reviewReply.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존잴하지 않는 리뷰댓글을 삭제하려 할 경우, 삭제에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "리뷰댓글삭제실패1";
            final String email = "리뷰댓글삭제실패1@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰댓글삭제실패1병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);
            final ReviewReply reviewReply = 리뷰댓글생성(member, review);


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/review-reply/{id}", 2222L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("리뷰댓글 수정")
    public class UpdateReviewReply {

        @Test
        @DisplayName("요청이 성공적으로 처리되어, 리뷰댓글이 수정되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "리뷰댓글수정성공";
            final String email = "리뷰댓글수정성공@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰댓글작성수정병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);
            final ReviewReply reviewReply = 리뷰댓글생성(member, review);


            final var request = new UpdateReviewReplyRequest("newReply");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/review-reply/{id}", reviewReply.getId())
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 리뷰댓글을 수정하려 할 경우, 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "리뷰댓글수정실패1";
            final String email = "리뷰댓글수정실패1@naver.com";
            final String password = "password12";
            final String hospitalName = "리뷰댓글수정실패1병원";

            //JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Member member = 회원생성(nickname, email, password);
            final Hospital hospital = 병원생성(hospitalName);
            final Review review = 리뷰생성(hospitalName, member);
            final ReviewReply reviewReply = 리뷰댓글생성(member, review);


            final var request = new UpdateReviewReplyRequest("newReply");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .patch("/api/review-reply/{id}", 1000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @NotNull
    private ReviewReply 리뷰댓글생성(final Member member, final Review review) {
        return reviewReplyRepository.save(
                ReviewReply.createReviewReply(member, review, "reply")
        );
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