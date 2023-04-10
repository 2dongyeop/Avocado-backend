package io.wisoft.capstonedesign.domain.reviewreply.service;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.CreateReviewReplyRequest;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.UpdateReviewReplyRequest;
import io.wisoft.capstonedesign.global.enumeration.status.ReviewReplyStatus;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullReviewReplyException;
import io.wisoft.capstonedesign.domain.reviewreply.application.ReviewReplyService;
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
public class ReviewReplyServiceTest {

    @Autowired EntityManager em;
    @Autowired ReviewReplyService reviewReplyService;

    @Test
    public void 리뷰_댓글_저장() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Review review = Review.builder()
                .member(member)
                .title("good")
                .body("good hospital")
                .starPoint(5)
                .target_hospital("아보카도")
                .build();
        em.persist(review);

        CreateReviewReplyRequest request = new CreateReviewReplyRequest(member.getId(), review.getId(), "저도 가봐야겠네요");

        //when -- 동작
        Long saveId = reviewReplyService.save(request);

        //then -- 검증
        ReviewReply reviewReply = reviewReplyService.findById(saveId);
        Assertions.assertThat(reviewReply.getStatus()).isEqualTo(ReviewReplyStatus.WRITE);
        Assertions.assertThat(reviewReply.getReply()).isEqualTo(request.getReply());
    }

    @Test
    public void 리뷰_댓글_삭제() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Review review = Review.builder()
                .member(member)
                .title("good")
                .body("good hospital")
                .starPoint(5)
                .target_hospital("아보카도")
                .build();
        em.persist(review);

        CreateReviewReplyRequest request = new CreateReviewReplyRequest(member.getId(), review.getId(), "저도 가봐야겠네요");
        Long saveId = reviewReplyService.save(request);

        //when -- 동작
        reviewReplyService.deleteReviewReply(saveId);

        //then -- 검증
        ReviewReply getReviewReply = reviewReplyService.findById(saveId);
        Assertions.assertThat(getReviewReply.getStatus()).isEqualTo(ReviewReplyStatus.DELETE);
    }

    @Test(expected = IllegalStateException.class)
    public void 리뷰_댓글_삭제요청_중복() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Review review = Review.builder()
                .member(member)
                .title("good")
                .body("good hospital")
                .starPoint(5)
                .target_hospital("아보카도")
                .build();
        em.persist(review);

        CreateReviewReplyRequest request = new CreateReviewReplyRequest(member.getId(), review.getId(), "저도 가봐야겠네요");
        Long saveId = reviewReplyService.save(request);

        //when -- 동작
        reviewReplyService.deleteReviewReply(saveId);
        reviewReplyService.deleteReviewReply(saveId);

        //then -- 검증
        fail("삭제요청 중복으로 인해 예외가 발생해야 한다.");
    }

    @Test(expected = NullReviewReplyException.class)
    public void 리뷰_댓글_단건조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        reviewReplyService.findById(100L);

        //then -- 검증
        fail("일치하는 리뷰댓글이 존재하지 않아 예외가 발생해야 한다.");
    }
    
    @Test
    public void 리뷰_댓글_수정() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Review review = Review.builder()
                .member(member)
                .title("good")
                .body("good hospital")
                .starPoint(5)
                .target_hospital("아보카도")
                .build();
        em.persist(review);

        CreateReviewReplyRequest request = new CreateReviewReplyRequest(member.getId(), review.getId(), "멋져요");
        Long saveId = reviewReplyService.save(request);

        //when -- 동작
        ReviewReply reviewReply = reviewReplyService.findById(saveId);
        UpdateReviewReplyRequest request2 = new UpdateReviewReplyRequest("짱 멋져요");
        reviewReplyService.updateReply(reviewReply.getId(), request2);

        //then -- 검증
        Assertions.assertThat(reviewReply.getReply()).isEqualTo(request2.getReply());
        Assertions.assertThat(reviewReply.getUpdatedAt()).isNotNull();
    }

    @Test(expected = IllegalValueException.class)
    public void 리뷰_댓글_수정_실패() throws Exception {
        //given -- 조건

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1")
                .email("email1")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        //병원 생성
        Review review = Review.builder()
                .member(member)
                .title("good")
                .body("good hospital")
                .starPoint(5)
                .target_hospital("아보카도")
                .build();
        em.persist(review);

        CreateReviewReplyRequest request = new CreateReviewReplyRequest(member.getId(), review.getId(), "멋져요");
        Long saveId = reviewReplyService.save(request);

        //when -- 동작
        ReviewReply reviewReply = reviewReplyService.findById(saveId);
        UpdateReviewReplyRequest request2 = new UpdateReviewReplyRequest(null);
        reviewReplyService.updateReply(reviewReply.getId(), request2);

        //then -- 검증
        fail("reply가 비어있어 예외가 발생해야 한다.");
    }
}