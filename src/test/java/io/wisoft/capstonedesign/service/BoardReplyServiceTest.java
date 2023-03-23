package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.board.Board;
import io.wisoft.capstonedesign.boardreply.BoardReply;
import io.wisoft.capstonedesign.boardreply.BoardReplyService;
import io.wisoft.capstonedesign.global.enumeration.status.BoardReplyStatus;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBoardReplyException;
import io.wisoft.capstonedesign.boardreply.BoardReplyRepository;
import io.wisoft.capstonedesign.hospital.Hospital;
import io.wisoft.capstonedesign.member.Member;
import io.wisoft.capstonedesign.staff.Staff;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardReplyServiceTest {

    @Autowired EntityManager em;
    @Autowired
    BoardReplyService boardReplyService;
    @Autowired BoardReplyRepository boardReplyRepository;

    @Test
    public void 게시글_댓글_저장() throws Exception {
        //given -- 조건

        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("lee", "lee@naver.com", "1111", "0000");
        em.persist(member);

        //게시글 생성
        HospitalDept dept = HospitalDept.OBSTETRICS;
        Board board = Board.createBoard(member, "title1", "body1", dept);
        em.persist(board);

        //회원이 다닐 병원 생성
        Hospital hospital = Hospital.createHospital("avocado", "04212345678", "대전시 유성구", "연중무휴");
        em.persist(hospital);

        //댓글을 생성할 의료진 생성
        HospitalDept hospitalDept = HospitalDept.OBSTETRICS;
        Staff staff = Staff.newInstance(hospital, "lim", "lsn@naver.com", "1111", "license", hospitalDept);
        em.persist(staff);

        //when -- 동작
        Long saveId = boardReplyService.save(board.getId(), staff.getId(), "안과가세요.");

        //then -- 검증
        Assertions.assertThat(boardReplyRepository.findOne(saveId).getStatus()).isEqualTo(BoardReplyStatus.WRITE);
    }

    @Test
    public void 게시글_댓글_삭제() throws Exception {
        //given -- 조건

        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("lee", "lee@naver.com", "1111", "0000");
        em.persist(member);

        //게시글 생성
        HospitalDept dept = HospitalDept.OBSTETRICS;
        Board board = Board.createBoard(member, "title1", "body1", dept);
        em.persist(board);

        //회원이 다닐 병원 생성
        Hospital hospital = Hospital.createHospital("avocado", "04212345678", "대전시 유성구", "연중무휴");
        em.persist(hospital);

        //댓글을 생성할 의료진 생성
        HospitalDept hospitalDept = HospitalDept.OBSTETRICS;
        Staff staff = Staff.newInstance(hospital, "lim", "lsn@naver.com", "1111", "license", hospitalDept);
        em.persist(staff);


        //게시글 댓글 생성
        Long saveId = boardReplyService.save(board.getId(), staff.getId(), "안과가세요.");

        //when -- 동작
        boardReplyService.deleteBoardReply(saveId);

        //then -- 검증
        Assertions.assertThat(boardReplyRepository.findOne(saveId).getStatus()).isEqualTo(BoardReplyStatus.DELETE);
    }

    @Test(expected = NullBoardReplyException.class)
    public void 게시글_댓글_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        boardReplyService.findOne(100L);

        //then -- 검증
        fail("예외가 발생해야 한다.");
    }

    //게시글 중복 삭제 요청
    @Test(expected = IllegalStateException.class)
    public void 게시글_댓글_삭제_중복_요청() throws Exception {
        //given -- 조건

        //게시글을 작성할 회원 생성
        Member member = Member.newInstance("lee", "lee@naver.com", "1111", "0000");
        em.persist(member);

        //게시글 생성
        HospitalDept dept = HospitalDept.OBSTETRICS;
        Board board = Board.createBoard(member, "title1", "body1", dept);
        em.persist(board);

        //회원이 다닐 병원 생성
        Hospital hospital = Hospital.createHospital("avocado", "04212345678", "대전시 유성구", "연중무휴");
        em.persist(hospital);

        //댓글을 생성할 의료진 생성
        HospitalDept hospitalDept = HospitalDept.OBSTETRICS;
        Staff staff = Staff.newInstance(hospital, "lim", "lsn@naver.com", "1111", "license", hospitalDept);
        em.persist(staff);

        //게시글 댓글 생성
        Long saveId = boardReplyService.save(board.getId(), staff.getId(), "안과가세요.");

        //when -- 동작
        boardReplyService.deleteBoardReply(saveId);
        boardReplyService.deleteBoardReply(saveId);

        //then -- 검증
        fail("삭제 요청 중복으로 인해 오류가 발생해야 한다.");
    }

    @Test
    public void 게시글_댓글_수정() throws Exception {
        //given -- 조건

        Member member = Member.newInstance("lee", "ldy@naver", "1111", "0000");
        em.persist(member);

        Board board = Board.createBoard(member, "title1", "body1", HospitalDept.OBSTETRICS);
        em.persist(board);

        Hospital hospital = Hospital.createHospital("avocado", "042", "대전", "always");
        em.persist(hospital);

        Staff staff = Staff.newInstance(hospital, "dong", "ldd@naver", "2222", "license_photo", HospitalDept.SURGICAL);
        em.persist(staff);

        BoardReply boardReply = BoardReply.createBoardReply(board, staff, "걱정말아요");
        em.persist(boardReply);

        //when -- 동작
        boardReplyService.update(boardReply.getId(), "걱정말긴 뭘 말아요!");

        //then -- 검증
        BoardReply getBoardReply = boardReplyService.findOne(boardReply.getId());
        Assertions.assertThat(getBoardReply.getReply()).isEqualTo("걱정말긴 뭘 말아요!");
        Assertions.assertThat(getBoardReply.getUpdateAt()).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void 게시글댓글_수정_실패() throws Exception {
        //given -- 조건

        Member member = Member.newInstance("lee", "ldy@naver", "1111", "0000");
        em.persist(member);

        Board board = Board.createBoard(member, "title1", "body1", HospitalDept.OBSTETRICS);
        em.persist(board);

        Hospital hospital = Hospital.createHospital("avocado", "042", "대전", "always");
        em.persist(hospital);

        Staff staff = Staff.newInstance(hospital, "dong", "ldd@naver", "2222", "license_photo", HospitalDept.SURGICAL);
        em.persist(staff);

        BoardReply boardReply = BoardReply.createBoardReply(board, staff, "걱정말아요");
        em.persist(boardReply);

        //when -- 동작
        boardReplyService.update(boardReply.getId(), null);

        //then -- 검증
        fail("댓글이 비어있어 예외가 발생해야 한다.");
    }
}