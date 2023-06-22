package io.wisoft.capstonedesign.domain.boardreply.web.restassured;

import io.restassured.RestAssured;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.board.persistence.BoardRepository;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReplyRepository;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.CreateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffRepository;
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

class BoardReplyApiControllerTest extends ApiTest {


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private BoardReplyRepository boardReplyRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisAdapter redisAdapter;


    @Nested
    @DisplayName("게시글댓글 저장")
    public class CreateBoardReply {

        @Test
        @DisplayName("요청이 성공적으로 처리되어 게시글에 댓글이 달려야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "게시글댓글저장성공";
            final String email = "게시글댓글저장성공@email.com";
            final String password = "password12";
            final String hospitalName = "게시글댓글저장성공병원";


            //헤더에 싣을 JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);


            //게시글을 작성할 회원 & 게시글 생성
            final Member member = 회원생성(nickname, email, password);
            final Board board = 게시글생성(member);

            //의료진이 다닐 병원 & 의료진 생성
            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);
            final var request = getRequest(board.getId(), staff.getId());


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .post("/api/board-reply")
                    .then()
                    .log().all().extract();


            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 게시글에 댓글을 작성하려 할 경우, 작성에 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "게시글댓글저장실패1";
            final String email = "게시글댓글저장실패1@email.com";
            final String password = "password12";
            final String hospitalName = "게시글댓글저장실패1병원";


            //헤더에 싣을 JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);


            //게시글을 작성할 회원 & 게시글 생성
            final Member member = 회원생성(nickname, email, password);
            게시글생성(member);

            //의료진이 다닐 병원 & 의료진 생성
            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);
            final var request = getRequest(10000L, staff.getId());


            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .post("/api/board-reply")
                    .then()
                    .log().all().extract();


            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }


        @Test
        @DisplayName("존재하지 않는 의료진 정보로 댓글을 작성하려 할 경우, 작성에 실패한다.")
        public void 실패2() throws Exception {

            //given -- 조건
            final String nickname = "게시글댓글저장실패2";
            final String email = "게시글댓글저장실패2@email.com";
            final String password = "password12";
            final String hospitalName = "게시글댓글저장실패2병원";


            //헤더에 싣을 JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);


            //게시글을 작성할 회원 & 게시글 생성
            final Member member = 회원생성(nickname, email, password);
            final Board board = 게시글생성(member);

            //의료진이 다닐 병원 & 의료진 생성
            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);
            final var request = getRequest(board.getId(), 10000L);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .post("/api/board-reply")
                    .then()
                    .log().all().extract();


            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }
    }


    @Nested
    @DisplayName("게시글댓글 삭제")
    public class DeleteBoardReply {

        @Test
        @DisplayName("요청이 성공적으로 수행될 경우, 게시글댓글이 삭제되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            final String nickname = "게시글댓글삭제성공";
            final String email = "게시글댓글삭제성공@email.com";
            final String password = "password12";
            final String hospitalName = "게시글댓글삭제성공병원";


            //헤더에 싣을 JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);


            //게시글을 작성할 회원 & 게시글 생성
            final Member member = 회원생성(nickname, email, password);
            final Board board = 게시글생성(member);

            //의료진이 다닐 병원 & 의료진 생성
            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            final Long boardReplyId = 게시글댓글저장(board, staff);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/board-reply/{id}", boardReplyId)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }


        @Test
        @DisplayName("존재하지 않는 게시글을 삭제하려 할 경우, 해당 요청은 실패한다.")
        public void 실패1() throws Exception {

            //given -- 조건
            final String nickname = "게시글댓글삭제실패1";
            final String email = "게시글댓글삭제실패1@email.com";
            final String password = "password12";
            final String hospitalName = "게시글댓글삭제실패1병원";


            //헤더에 싣을 JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);


            //게시글을 작성할 회원 & 게시글 생성
            final Member member = 회원생성(nickname, email, password);
            final Board board = 게시글생성(member);

            //의료진이 다닐 병원 & 의료진 생성
            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            final Long boardReplyId = 게시글댓글저장(board, staff);

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .when()
                    .delete("/api/board-reply/{id}", 10000L)
                    .then()
                    .log().all().extract();

            //then -- 검증
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            Assertions.assertThat((String) response.jsonPath().get("message")).isEqualTo("해당 엔티티를 찾을 수 없습니다.");
        }
    }


    @Nested
    @DisplayName("게시글댓글 수정")
    public class UpdateBoardReply {

        @Test
        @DisplayName("요청이 성공적으로 수행될 경우, 게시글댓글이 수정되어야 한다.")
        public void 성공() throws Exception {

            //given -- 조건
            //given -- 조건
            final String nickname = "게시글댓글수정성공";
            final String email = "게시글댓글수정성공@email.com";
            final String password = "password12";
            final String hospitalName = "게시글댓글수정성공병원";


            //헤더에 싣을 JWT
            final String accessToken = jwtTokenProvider.createAccessToken(email);
            redisAdapter.setValue(email, accessToken, 3600000, TimeUnit.SECONDS);


            //게시글을 작성할 회원 & 게시글 생성
            final Member member = 회원생성(nickname, email, password);
            final Board board = 게시글생성(member);

            //의료진이 다닐 병원 & 의료진 생성
            final Hospital hospital = 병원생성(hospitalName);
            final Staff staff = 의료진생성(nickname, email, password, hospital);

            final Long boardReplyId = 게시글댓글저장(board, staff);

            final var request = new CreateBoardReplyRequest(board.getId(), staff.getId(), "new-reply");

            //when -- 동작
            final var response = RestAssured
                    .given()
                    .log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "bearer " + accessToken)
                    .body(request)
                    .when()
                    .patch("/api/board-reply/{id}", boardReplyId)
                    .then()
                    .log().all().extract();

            //then -- 검증

            final BoardReply boardReply = boardReplyRepository.findById(boardReplyId).get();

            Assertions.assertThat(boardReply.getReply()).isEqualTo("new-reply");
            Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }
    }


    @NotNull
    private Long 게시글댓글저장(Board board, Staff staff) {
        return boardReplyRepository.save(BoardReply.createBoardReply(
                board,
                staff,
                "reply"
        )).getId();
    }


    @NotNull
    private CreateBoardReplyRequest getRequest(final Long boardId, final Long staffId) {
        return new CreateBoardReplyRequest(boardId, staffId ,"reply");
    }

    @NotNull
    private Member 회원생성(final String nickname, final String email, final String password) {
        return memberRepository.save(Member.newInstance(nickname + "멤버", email + "멤버", password, "phone"));
    }

    @NotNull
    private Staff 의료진생성(final String nickname, final String email, final String password, final Hospital hospital) {
        return staffRepository.save(Staff.newInstance(
                hospital,
                nickname + "스탶",
                email + "스탶",
                password,
                "licensse",
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
    private Board 게시글생성(final Member member) {
        return boardRepository.save(Board.createBoard(
                member,
                "title",
                "body",
                HospitalDept.DENTAL
        ));
    }
}