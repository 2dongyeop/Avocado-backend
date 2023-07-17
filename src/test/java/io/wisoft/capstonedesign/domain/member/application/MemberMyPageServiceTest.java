package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class MemberMyPageServiceTest extends ServiceTest {

    @Autowired
    MemberMyPageService memberMyPageService;

    @Test
    public void findReviewsByMemberIdUsingPaging() throws Exception {
        //given -- 조건
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<Review> list = memberMyPageService.findReviewsByMemberIdUsingPaging(1L, request)
                .getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isGreaterThan(1);
    }

    @Test
    public void findBoardsByMemberIdUsingPaging() throws Exception {
        //given
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<Board> list = memberMyPageService.findBoardsByMemberIdUsingPaging(1L, request)
                .getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void findAppointmentsByMemberId() throws Exception {
        //given
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<Appointment> list = memberMyPageService.findAppointmentsByMemberId(1L);

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void findPicksByMemberIdUsingPaging() throws Exception {
        //given
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<Pick> list = memberMyPageService.findPicksByMemberIdUsingPaging(1L, request)
                .getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}