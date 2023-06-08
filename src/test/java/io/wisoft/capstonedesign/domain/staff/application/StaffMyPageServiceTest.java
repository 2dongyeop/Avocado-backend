package io.wisoft.capstonedesign.domain.staff.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StaffMyPageServiceTest extends ServiceTest {

    @Autowired EntityManager em;
    @Autowired StaffMyPageService staffMyPageService;

    @Test
    public void 자신이_속한_병원의_리뷰_목록_조회() throws Exception {
        //given -- 조건

        //when -- 동작
        final List<Review> reviewListByHospitalName = staffMyPageService.findReviewByStaffHospitalName(1L);

        //then -- 검증
        Assertions.assertThat(reviewListByHospitalName.size()).isEqualTo(1);
    }

    @Test
    public void 자신이_댓글을_작성한_게시글_목록_조회() throws Exception {
        //given -- 조건

        //when -- 동작
        final List<Board> boardList = staffMyPageService.findBoardListByStaffId(1L);

        //then -- 검증
        Assertions.assertThat(boardList.size()).isEqualTo(2L);
    }
}