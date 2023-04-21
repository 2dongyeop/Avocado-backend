package io.wisoft.capstonedesign.domain.reviewreply.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewReplyRepositoryTest {

    @Autowired ReviewReplyRepository reviewReplyRepository;

    @Test
    public void findReviewReplyByReviewId() throws Exception {
        //given -- 조건


        //when -- 동작
        List<ReviewReply> list = reviewReplyRepository.findByReviewId(1L);

        //then -- 검증
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("특정 리뷰의 댓글 목록 내림차순 조회")
    public void findAllOrderByCreatedAtDesc() throws Exception {
        //given -- 조건


        //when -- 동작
        List<ReviewReply> list = reviewReplyRepository.findAllOrderByCreatedAtDesc(1L);

        //then -- 검증
        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}