package io.wisoft.capstonedesign.domain.review.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    public void paging() throws Exception {
        //given -- 조건
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final Page<Review> page = reviewRepository.findByUsingPaging(request);
        final List<Review> content = page.getContent();

        //then -- 검증
        assertThat(content.size()).isGreaterThan(1); //콘텐트 사이즈
    }
}