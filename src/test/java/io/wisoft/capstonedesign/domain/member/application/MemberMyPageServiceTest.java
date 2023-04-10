package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberMyPageServiceTest {

    @Autowired MemberMyPageService memberMyPageService;

    @Test
    public void findReviewsByMemberIdUsingPaging() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        List<Review> list = memberMyPageService.findReviewsByMemberIdUsingPaging(1L, request)
                .getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void findBoardsByMemberIdUsingPaging() throws Exception {
        //given
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        List<Board> list = memberMyPageService.findBoardsByMemberIdUsingPaging(1L, request)
                .getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void findAppointmentsByMemberId() throws Exception {
        //given
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        List<Appointment> list = memberMyPageService.findAppointmentsByMemberId(1L);

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void findPicksByMemberIdUsingPaging() throws Exception {
        //given
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        List<Pick> list = memberMyPageService.findPicksByMemberIdUsingPaging(1L, request)
                .getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}