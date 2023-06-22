package io.wisoft.capstonedesign.domain.board.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.board.web.BoardApiController;
import io.wisoft.capstonedesign.domain.board.web.dto.CreateBoardRequest;
import io.wisoft.capstonedesign.domain.board.web.dto.UpdateBoardRequest;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
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

public class BoardApiControllerTest extends ApiTest {

    @Autowired
    private BoardApiController boardApiController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;

    @Nested
    @DisplayName("게시글 저장")
    public class CreateBoard {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 게시글이 저장되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "게시글저장성공";
            final String email = "게시글저장성공@email.com";
            final String password = "password12";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);

            final var request = getCreateBoardRequest(memberId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .post("/api/boards")
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            Assertions.assertThat((Integer) response.jsonPath().get("id")).isPositive();
        }
    }


    @Nested
    @DisplayName("게시글 수정")
    public class UpdateBoard {

        @Test
        @DisplayName("수정 요청시 게시글이 수정되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "게시글수정성공";
            final String email = "게시글수정성공@email.com";
            final String password = "password12";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final Long boardId = 게시글생성(memberId);

            final var request = new UpdateBoardRequest("new new title", "new new body");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .patch("/api/boards/{id}", boardId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            Assertions.assertThat((String) response.jsonPath().get("newTitle")).isEqualTo("new new title");
            Assertions.assertThat((String) response.jsonPath().get("newBody")).isEqualTo("new new body");
        }


        @Test
        @DisplayName("존재하지 않는 게시글의 정보를 수정하려 할 경우 수정에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "게시글수정실패1";
            final String email = "게시글수정실패1@email.com";
            final String password = "password12";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final Long boardId = 게시글생성(memberId);

            final var request = new UpdateBoardRequest("new new title", "new new body");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .patch("/api/boards/{id}", 10000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }
    }


    @Nested
    @DisplayName("게시글 삭제")
    public class DeleteBoard {

        @Test
        @DisplayName("삭제 요청시 게시글이 삭제되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "게시글삭제성공";
            final String email = "게시글삭제성공@email.com";
            final String password = "password12";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final Long boardId = 게시글생성(memberId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/boards/{id}", boardId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 게시글을 삭제하려 할 경우, 삭제에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "게시글삭제실패1";
            final String email = "게시글삭제실패1@email.com";
            final String password = "password12";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final Long boardId = 게시글생성(memberId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/boards/{id}", 10000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("이미 삭제된 게시글을 중복 삭제하려할 경우 삭제에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String nickname = "게시글삭제실패2";
            final String email = "게시글삭제실패2@email.com";
            final String password = "password12";

            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);

            final Long memberId = 회원생성(nickname, email, password);
            final Long boardId = 게시글생성(memberId);

            boardApiController.deleteBoard(boardId);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/boards/{id}", boardId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("State is something wrong");
        }
    }


    private Long 게시글생성(final Long memberId) {
        final var createBoardRequest = getCreateBoardRequest(memberId);
        return boardApiController.createBoard(createBoardRequest).id();
    }


    @NotNull
    private CreateBoardRequest getCreateBoardRequest(final Long memberId) {
        return new CreateBoardRequest(
                memberId,
                "title",
                "body",
                "DENTAL",
                "boardPath"
        );
    }

    private Long 회원생성(final String nickname, final String email, final String password) {
        return memberRepository.save(Member.newInstance(nickname, email, password, "phone")).getId();
    }
}
