package io.wisoft.capstonedesign.domain.member.persistence;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberMyPageRepositoryTest {

    @Autowired MemberMyPageRepository memberMyPageRepository;

    @Test
    public void findReviewsByMemberIdUsingPaging() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        Page<Review> page = memberMyPageRepository.findReviewsByMemberIdUsingPaging(1L, request);
        List<Review> list = page.getContent();

        //then -- 검증
        assertThat(list.size()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
    }

    @Test
    public void findBoardsByMemberIdUsingPaging() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        Page<Board> page = memberMyPageRepository.findBoardsByMemberIdUsingPaging(1L, request);
        List<Board> list = page.getContent();

        //then -- 검증
        assertThat(list.size()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
    }

    @Test
    public void findAppointmentsByMemberId() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        List<Appointment> list = memberMyPageRepository.findAppointmentsByMemberId(1L);

        //then -- 검증
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void findPicksByMemberIdUsingPaging() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        Page<Pick> page = memberMyPageRepository.findPicksByMemberIdUsingPaging(1L, request);
        List<Pick> list = page.getContent();

        //then -- 검증
        assertThat(list.size()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
    }
}