package io.wisoft.capstonedesign.domain.review.application;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.review.web.dto.CreateReviewRequest;
import io.wisoft.capstonedesign.domain.review.web.dto.UpdateReviewRequest;
import io.wisoft.capstonedesign.global.enumeration.status.ReviewStatus;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullReviewException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.wisoft.capstonedesign.global.data.MemberTestData.getDefaultMember;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired EntityManager em;
    @Autowired ReviewService reviewService;

    @Test
    public void 리뷰작성() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateReviewRequest request = getCreateReviewRequest(member, 5, "서울대병원");

        //when -- 동작
        final Long saveId = reviewService.save(request);

        //then -- 검증
        final Review getReview = reviewService.findById(saveId); //저장된 리뷰

        Assertions.assertThat(getReview.getStatus()).isEqualTo(ReviewStatus.WRITE);
    }

    @Test
    public void 리뷰삭제() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //리뷰 생성
        final CreateReviewRequest request = getCreateReviewRequest(member, 5, "서울대병원");
        final Long saveId = reviewService.save(request);

        //when -- 동작
        reviewService.deleteReview(saveId);

        //then -- 검증
        final Review getReview = reviewService.findById(saveId);

        Assertions.assertThat(getReview.getStatus()).isEqualTo(ReviewStatus.DELETE);
    }

    //리뷰 중복 삭제요청
    @Test
    public void 리뷰_삭제_중복요청() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //리뷰 생성
        final CreateReviewRequest request = getCreateReviewRequest(member, 5, "서울대병원");
        final Long saveId = reviewService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalStateException.class, () -> {
            reviewService.deleteReview(saveId);
            reviewService.deleteReview(saveId);
        });
    }

    //리뷰 별점 1~5만족?
    @Test
    public void 리뷰_별점_범위초과() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);


        //when -- 동작
        //then -- 검증

        assertThrows(IllegalValueException.class, () -> {
            final CreateReviewRequest request = getCreateReviewRequest(member, 10, "아보카도 병원");
            Long saveId = reviewService.save(request);
        });
    }

    @Test
    public void 리뷰_단건_조회_실패() throws Exception {
        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateReviewRequest request = getCreateReviewRequest(member, 5, "서울대병원");
        final Long saveId = reviewService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullReviewException.class, () -> {
            Review review = reviewService.findById(100L);
        });
    }

    @Test
    public void 리뷰_수정() throws Exception {

        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateReviewRequest request1 = getCreateReviewRequest(member, 5, "서울대병원");
        final Long saveId = reviewService.save(request1);
        final Review review = reviewService.findById(saveId);

        //when -- 동작

        final UpdateReviewRequest request2 = new UpdateReviewRequest("제목2", "본문1");

        reviewService.updateTitleBody(review.getId(), request2);

        //then -- 검증
        Assertions.assertThat(review.getTitle()).isEqualTo(request2.newTitle());
        Assertions.assertThat(review.getUpdatedAt()).isNotNull();
    }

    @Test
    public void 리뷰_수정_실패() throws Exception {

        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        final CreateReviewRequest request1 = getCreateReviewRequest(member, 5, "아보카도 병원");
        final Long saveId = reviewService.save(request1);
        final Review review = reviewService.findById(saveId);

        //when -- 동작
        final UpdateReviewRequest request2 = new UpdateReviewRequest(null, "본문1");

        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            reviewService.updateTitleBody(review.getId(), request2);
        });
    }


    @Test
    public void 특정_병원의_리뷰_조회() throws Exception {

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //리뷰생성
        final CreateReviewRequest request1 = getCreateReviewRequest(member, 5, "서울대병원");
        final Long saveId = reviewService.save(request1);
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final Page<Review> page = reviewService.findByTargetHospital("을지대학병원", request);
        final List<Review> list = page.getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void 특정_병원의_리뷰_조회_실패() throws Exception {

        //given -- 조건

        //회원 생성
        final Member member = getDefaultMember();
        em.persist(member);

        //리뷰생성
        final CreateReviewRequest request1 = getCreateReviewRequest(member, 5, "서울대병원");
        final Long saveId = reviewService.save(request1);
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            reviewService.findByTargetHospital("아보카두두병원", request);
        });
    }

    private static CreateReviewRequest getCreateReviewRequest(final Member member, final int starPoint, final String hospitalName) {
        return new CreateReviewRequest(member.getId(), "친절해요", "자세히 진료해줘요", starPoint, hospitalName, "사진_링크");
    }
}