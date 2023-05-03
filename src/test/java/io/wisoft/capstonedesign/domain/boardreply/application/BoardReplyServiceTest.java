package io.wisoft.capstonedesign.domain.boardreply.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.CreateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.UpdateBoardReplyRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardReplyException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardReplyServiceTest {

    @Autowired EntityManager em;
    @Autowired BoardReplyService boardReplyService;

    @Test
    public void 게시글_댓글_저장() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성
        final Board board = Board.builder()
                .member(member)
                .title("title1")
                .body("body1")
                .dept(HospitalDept.OBSTETRICS)
                .build();
        em.persist(board);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name("name1")
                .email("email1")
                .password("pass1")
                .license_path("licen1")
                .dept(HospitalDept.DENTAL)
                .build();
        em.persist(staff);

        final CreateBoardReplyRequest request = CreateBoardReplyRequest.builder()
                .boardId(board.getId())
                .staffId(staff.getId())
                .reply("reply")
                .build();

        //when -- 동작
        final Long saveId = boardReplyService.save(request);

        //then -- 검증
        final BoardReply boardReply = boardReplyService.findById(saveId);
        Assertions.assertThat(boardReply.getReply()).isEqualTo(request.reply());
    }

    @Test
    public void 게시글_댓글_삭제() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성
        final Board board = Board.builder()
                .member(member)
                .title("title1")
                .body("body1")
                .dept(HospitalDept.OBSTETRICS)
                .build();
        em.persist(board);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name("name1")
                .email("email1")
                .password("pass1")
                .license_path("licen1")
                .dept(HospitalDept.DENTAL)
                .build();
        em.persist(staff);


        //게시글 댓글 생성
        final CreateBoardReplyRequest request = CreateBoardReplyRequest.builder()
                .boardId(board.getId())
                .staffId(staff.getId())
                .reply("reply")
                .build();
        final Long saveId = boardReplyService.save(request);

        //when -- 동작
        boardReplyService.deleteBoardReply(saveId);

        //then -- 검증
        assertThrows(NullBoardReplyException.class, () -> {
            boardReplyService.findById(saveId);
        });
    }

    @Test
    public void 게시글_댓글_조회_실패() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성
        final Board board = Board.builder()
                .member(member)
                .title("title1")
                .body("body1")
                .dept(HospitalDept.OBSTETRICS)
                .build();
        em.persist(board);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name("name1")
                .email("email1")
                .password("pass1")
                .license_path("licen1")
                .dept(HospitalDept.DENTAL)
                .build();
        em.persist(staff);

        //게시글 댓글 생성
        final CreateBoardReplyRequest request = CreateBoardReplyRequest.builder()
                .boardId(board.getId())
                .staffId(staff.getId())
                .reply("reply")
                .build();
        final Long saveId = boardReplyService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullBoardReplyException.class, () -> {
            boardReplyService.findById(100L);
        });
    }

    @Test
    public void 게시글_댓글_삭제_중복_요청() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성
        final Board board = Board.builder()
                .member(member)
                .title("title1")
                .body("body1")
                .dept(HospitalDept.OBSTETRICS)
                .build();
        em.persist(board);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name("name1")
                .email("email1")
                .password("pass1")
                .license_path("licen1")
                .dept(HospitalDept.DENTAL)
                .build();
        em.persist(staff);

        //게시글 댓글 생성
        final CreateBoardReplyRequest request = CreateBoardReplyRequest.builder()
                .boardId(board.getId())
                .staffId(staff.getId())
                .reply("reply")
                .build();
        final Long saveId = boardReplyService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullBoardReplyException.class, () -> {
            boardReplyService.deleteBoardReply(saveId);
            boardReplyService.deleteBoardReply(saveId);
        });
    }

    @Test
    public void 게시글_댓글_수정() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성
        final Board board = Board.builder()
                .member(member)
                .title("title1")
                .body("body1")
                .dept(HospitalDept.OBSTETRICS)
                .build();
        em.persist(board);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name("name1")
                .email("email1")
                .password("pass1")
                .license_path("licen1")
                .dept(HospitalDept.DENTAL)
                .build();
        em.persist(staff);

        //게시글 댓글 생성
        final CreateBoardReplyRequest request1 = CreateBoardReplyRequest.builder()
                .boardId(board.getId())
                .staffId(staff.getId())
                .reply("reply")
                .build();
        final Long saveId = boardReplyService.save(request1);

        final UpdateBoardReplyRequest request2 = new UpdateBoardReplyRequest("걱정말긴 뭘 말아요!");

        //when -- 동작
        final BoardReply boardReply = boardReplyService.findById(saveId);
        boardReplyService.update(boardReply.getId(), request2);

        //then -- 검증
        final BoardReply getBoardReply = boardReplyService.findById(boardReply.getId());
        Assertions.assertThat(getBoardReply.getReply()).isEqualTo("걱정말긴 뭘 말아요!");
        Assertions.assertThat(getBoardReply.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 게시글댓글_수정_실패() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //게시글 생성
        final Board board = Board.builder()
                .member(member)
                .title("title1")
                .body("body1")
                .dept(HospitalDept.OBSTETRICS)
                .build();
        em.persist(board);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = Staff.builder()
                .hospital(hospital)
                .name("name1")
                .email("email1")
                .password("pass1")
                .license_path("licen1")
                .dept(HospitalDept.DENTAL)
                .build();
        em.persist(staff);

        //게시글 댓글 생성
        final CreateBoardReplyRequest request1 = CreateBoardReplyRequest.builder()
                .boardId(board.getId())
                .staffId(staff.getId())
                .reply("reply")
                .build();
        final Long saveId = boardReplyService.save(request1);


        final UpdateBoardReplyRequest request2 = new UpdateBoardReplyRequest(null);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            final BoardReply boardReply = boardReplyService.findById(saveId);
            boardReplyService.update(boardReply.getId(), request2);
        });
    }
}