package io.wisoft.capstonedesign.service;


import io.wisoft.capstonedesign.member.Member;
import io.wisoft.capstonedesign.review.Review;
import io.wisoft.capstonedesign.global.enumeration.status.ReviewStatus;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullReviewException;
import io.wisoft.capstonedesign.review.ReviewRepository;
import io.wisoft.capstonedesign.review.ReviewService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired EntityManager em;
    @Autowired
    ReviewService reviewService;
    @Autowired ReviewRepository reviewRepository;

    @Test
    public void 리뷰작성() throws Exception {
        //given -- 조건

        //리뷰를 작성할 회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        //when -- 동작
        Long saveId = reviewService.save(member.getId(), "친절해요", "자세히 진료해줘요", "사진_링크", 5, "아보카도 병원");

        //then -- 검증
        Review getReview = reviewRepository.findOne(saveId); //저장된 리뷰

        Assertions.assertThat(getReview.getStatus()).isEqualTo(ReviewStatus.WRITE);
    }

    //리뷰삭제
    @Test
    public void 리뷰삭제() throws Exception {
        //given -- 조건

        //리뷰를 작성할 회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        //리뷰 생성
        Long saveId = reviewService.save(member.getId(), "친절해요", "자세히 진료해줘요", "사진_링크", 5, "아보카도 병원");

        //when -- 동작
        reviewService.deleteReview(saveId);

        //then -- 검증
        Review getReview = reviewService.findOne(saveId);

        Assertions.assertThat(getReview.getStatus()).isEqualTo(ReviewStatus.DELETE);
    }

    //리뷰 중복 삭제요청
    @Test(expected = IllegalStateException.class)
    public void 리뷰_삭제_중복요청() throws Exception {
        //given -- 조건

        //리뷰를 작성할 회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        //리뷰 생성
        Long saveId = reviewService.save(member.getId(), "친절해요", "자세히 진료해줘요", "사진_링크", 5, "아보카도 병원");

        //when -- 동작
        reviewService.deleteReview(saveId);
        reviewService.deleteReview(saveId);

        //then -- 검증
        fail("중복 삭제 요청으로 인한 예외가 발생해야 한다.");
    }

    //리뷰 별점 1~5만족?
    @Test(expected = IllegalValueException.class)
    public void 리뷰_별점_범위초과() throws Exception {
        //given -- 조건
        //리뷰를 작성할 회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        int starPoint = 6;

        //when -- 동작
        Long saveId = reviewService.save(member.getId(), "친절해요", "자세히 진료해줘요", "사진_링크", starPoint, "아보카도 병원");

        //then -- 검증
        fail("리뷰의 별점이 1~5 사이의 범위가 아니므로 예외가 발생해야 한다.");
    }

    @Test(expected = NullReviewException.class)
    public void 리뷰_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Review review = reviewService.findOne(100L);

        //then -- 검증
        fail("해당 reviewId에 일치하는 리뷰 정보가 없어 예외가 발생해야 한다.");
    }

    @Test
    public void 리뷰_수정() throws Exception {

        //given -- 조건
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        Review review = Review.createReview(member, "제목1", "본문1", 4, "avocado");
        em.persist(review);

        //when -- 동작
        reviewService.updateTitleBody(review.getId(), "제목2", "본문1");

        //then -- 검증
        Assertions.assertThat(review.getTitle()).isEqualTo("제목2");
        Assertions.assertThat(review.getUpdateAt()).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void 리뷰_수정_실패() throws Exception {

        //given -- 조건
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        Review review = Review.createReview(member, "제목1", "본문1", 4, "avocado");
        em.persist(review);

        //when -- 동작
        reviewService.updateTitleBody(review.getId(), null, "본문1");

        //then -- 검증
        fail("제목이 없어 예외가 발생해야 한다.");
    }

    @Test(expected = IllegalValueException.class)
    public void 특정_병원의_리뷰_조회_실패() throws Exception {

        //given -- 조건

        //리뷰를 작성할 회원 생성
        Member member = Member.newInstance("lee", "ldy_1204@naver.com", "1111", "0000");
        em.persist(member);

        //리뷰생성
        Long saveId = reviewService.save(member.getId(), "친절해요", "자세히 진료해줘요", "사진_링크", 5, "아보카도 병원");

        //when -- 동작
        reviewService.findByTargetHospital("아보카두두병원");

        //then -- 검증
        fail("해당 이름을 가진 병원 리뷰가 존재하지 않아 예외가 발생해야 한다.");
    }
}