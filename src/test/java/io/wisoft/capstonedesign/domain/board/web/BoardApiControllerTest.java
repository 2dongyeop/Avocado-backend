package io.wisoft.capstonedesign.domain.board.web;

import io.wisoft.capstonedesign.domain.board.web.dto.*;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.wisoft.capstonedesign.global.data.MemberTestData.getDefaultMember;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardApiControllerTest {

    @Autowired EntityManager em;
    @Autowired BoardApiController boardApiController;

    @Test
    public void createBoard_success() throws Exception {
        //given -- 조건

        final Member member = getDefaultMember();
        em.persist(member);

        final CreateBoardRequest request = getCreateBoardRequest(member);

        //when -- 동작
        final CreateBoardResponse response = boardApiController.createBoard(request);

        //then -- 검증
        Assertions.assertThat(response.id()).isNotNull();
    }


    @Test
    public void updateBoardTitleBody_success() throws Exception {
        //given -- 조건
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateBoardRequest request1 = getCreateBoardRequest(member);

        final CreateBoardResponse response = boardApiController.createBoard(request1);

        final UpdateBoardRequest request2 = new UpdateBoardRequest("newTitle", "newBody");

        //when -- 동작
        final UpdateBoardResponse updateResponse = boardApiController.updateBoardTitleBody(response.id(), request2);

        //then -- 검증
        Assertions.assertThat(updateResponse.id()).isNotNull();
    }

    @Test
    public void deleteBoard_success() throws Exception {
        //given -- 조건
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateBoardRequest request1 = getCreateBoardRequest(member);

        final CreateBoardResponse createBoardResponse = boardApiController.createBoard(request1);

        //when -- 동작
        final DeleteBoardResponse deleteBoardResponse = boardApiController.deleteBoard(createBoardResponse.id());

        //then -- 검증
        Assertions.assertThat(deleteBoardResponse.id()).isNotNull();
    }

    @Test
    public void boardsUsingPaging_success() throws Exception {
        //given -- 조건
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateBoardRequest request1 = getCreateBoardRequest(member);

        boardApiController.createBoard(request1);

        //when -- 동작
        final PageRequest pageRequest = PageRequest.of(0, 10);
        final Page<BoardListDto> page = boardApiController.boardsUsingPaging(pageRequest);
        final List<BoardListDto> content = page.getContent();

        //then -- 검증
        Assertions.assertThat(content.size()).isGreaterThan(1);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(page.hasNext()).isFalse();
    }

    private static CreateBoardRequest getCreateBoardRequest(final Member member) {
        return CreateBoardRequest.builder()
                .memberId(member.getId())
                .title("title")
                .body("body")
                .dept("DENTAL")
                .boardPhotoPath("path")
                .build();
    }
}