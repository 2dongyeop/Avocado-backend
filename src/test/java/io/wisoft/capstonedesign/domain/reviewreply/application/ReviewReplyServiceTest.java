package io.wisoft.capstonedesign.domain.reviewreply.application;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.CreateReviewReplyRequest;
import io.wisoft.capstonedesign.domain.reviewreply.web.dto.UpdateReviewReplyRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullReviewReplyException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Assertions.assertThat(reviewReply.getReply()).isEqualTo(request.reply());
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
        assertThrows(NullReviewReplyException.class, () -> {
            reviewReplyService.findById(saveId);
        });
    }

    @Test
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
        //then -- 검증
        assertThrows(NullReviewReplyException.class, () -> {
            reviewReplyService.deleteReviewReply(saveId);
            reviewReplyService.deleteReviewReply(saveId);
        });
    }


    @Test
    public void 리뷰_단건조회_성공() throws Exception {

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

        //when -- 동작
        ReviewReply reviewReply = reviewReplyService.findById(saveId);

        //then -- 검증
        Assertions.assertThat(reviewReply.getReply()).isEqualTo("저도 가봐야겠네요");
    }


    @Test
    public void 리뷰_댓글_단건조회_실패() throws Exception {
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

        //when -- 동작
        //then -- 검증
        assertThrows(NullReviewReplyException.class, () -> {
            reviewReplyService.findById(100L);
        });
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
        Assertions.assertThat(reviewReply.getReply()).isEqualTo(request2.reply());
        Assertions.assertThat(reviewReply.getUpdatedAt()).isNotNull();
    }

    @Test
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


        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            reviewReplyService.updateReply(reviewReply.getId(), request2);
        });
    }


    @Test
    public void 특정_리뷰의_리뷰댓글_목록_조회() throws Exception {
        //given -- 조건


        //when -- 동작
        List<ReviewReply> list = reviewReplyService.findByReviewId(1L);

        //then -- 검증
        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}