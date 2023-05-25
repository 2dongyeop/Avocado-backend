package io.wisoft.capstonedesign.domain.boardreply.web;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.boardreply.web.dto.*;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static io.wisoft.capstonedesign.global.data.BoardTestData.getDefaultBoard;
import static io.wisoft.capstonedesign.global.data.HospitalTestData.getDefaultHospital;
import static io.wisoft.capstonedesign.global.data.MemberTestData.getDefaultMember;
import static io.wisoft.capstonedesign.global.data.StaffTestData.getDefaultStaff;

@SpringBootTest
@Transactional
public class BoardReplyApiControllerTest {

    @Autowired EntityManager em;
    @Autowired BoardReplyApiController boardReplyApiController;

    @Test
    public void createBoardReply_success() throws Exception {
        //given -- 조건
        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성
        final Board board = getDefaultBoard(member);
        em.persist(board);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = getDefaultStaff(hospital);
        em.persist(staff);

        final CreateBoardReplyRequest request = getCreateBoardReplyRequest(board, staff);

        //when -- 동작
        CreateBoardReplyResponse createBoardReplyResponse = boardReplyApiController.createBoardReply(request);

        //then -- 검증
        Assertions.assertThat(createBoardReplyResponse.id()).isNotNull();
    }


    @Test
    public void updateBoardReply() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성
        final Board board = getDefaultBoard(member);
        em.persist(board);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = getDefaultStaff(hospital);
        em.persist(staff);

        final CreateBoardReplyRequest request = getCreateBoardReplyRequest(board, staff);

        final CreateBoardReplyResponse createBoardReplyResponse = boardReplyApiController.createBoardReply(request);

        final UpdateBoardReplyRequest updateBoardReplyRequest = new UpdateBoardReplyRequest("newReply");

        //when -- 동작
        final UpdateBoardReplyResponse updateBoardReplyResponse = boardReplyApiController.updateBoardReply(createBoardReplyResponse.id(), updateBoardReplyRequest);

        //then -- 검증
        Assertions.assertThat(updateBoardReplyResponse.id()).isNotNull();
    }

    @Test
    public void deleteBoardReply_success() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성
        final Board board = getDefaultBoard(member);
        em.persist(board);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = getDefaultStaff(hospital);
        em.persist(staff);

        final CreateBoardReplyRequest request = getCreateBoardReplyRequest(board, staff);

        final CreateBoardReplyResponse createBoardReplyResponse = boardReplyApiController.createBoardReply(request);

        //when -- 동작
        DeleteBoardReplyResponse deleteBoardReplyResponse = boardReplyApiController.deleteBoardReply(createBoardReplyResponse.id());

        //then -- 검증
        Assertions.assertThat(deleteBoardReplyResponse.id()).isNotNull();
    }

    @Test
    public void boardReply_success() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //게시글 생성
        final Board board = getDefaultBoard(member);
        em.persist(board);

        //병원 생성
        final Hospital hospital = getDefaultHospital();
        em.persist(hospital);

        //의료진 생성
        final Staff staff = getDefaultStaff(hospital);
        em.persist(staff);

        final CreateBoardReplyRequest request = getCreateBoardReplyRequest(board, staff);

        final CreateBoardReplyResponse createBoardReplyResponse = boardReplyApiController.createBoardReply(request);

        //when -- 동작
        Result result = boardReplyApiController.boardReply(createBoardReplyResponse.id());

        //then -- 검증
        Assertions.assertThat(result.data()).isNotNull();
    }

    private static CreateBoardReplyRequest getCreateBoardReplyRequest(final Board board, final Staff staff) {
        return CreateBoardReplyRequest.builder()
                .boardId(board.getId())
                .staffId(staff.getId())
                .reply("reply")
                .build();
    }

}