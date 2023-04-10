package io.wisoft.capstonedesign.domain.reviewreply.persistence;

import org.assertj.core.api.Assertions;
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
}