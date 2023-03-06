package io.wisoft.capstonedesign.domain;

import io.wisoft.capstonedesign.domain.enumeration.ReviewStatus;
import io.wisoft.capstonedesign.exception.IllegalValueException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id @GeneratedValue()
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_title", nullable = false)
    private String title;

    //pg 사용시 @Lob 지우고, @Column(nullable = false, columnDefinition="TEXT")로 바꾸기
    @Lob
    @Column(name = "review_body", nullable = false)
    private String body;

    @CreatedDate
    @Column(name = "review_create_at", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "review_update_at")
    private LocalDateTime updateAt;

    @Column(name = "review_photo_path")
    private String reviewPhotoPath;

    @Column(name = "star_point", nullable = false)
    private int starPoint;  //TODO: 1~5의 정수만 들어가도록 유효성 검사 추가요망

    @Column(name = "target_hospital", nullable = false)
    private String target_hospital;

    @Column(name = "review_status")
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<ReviewReply> reviewReplyList = new ArrayList<>();

    /* 연관관계 편의 메서드 */
    public void setMember(Member member) {
        //comment: 기존 관계 제거
        if (this.member != null) {
            this.member.getReviewList().remove(this);
        }

        this.member = member;

        //comment: 무한루프 방지
        if (!member.getReviewList().contains(this)) {
            member.getReviewList().add(this);
        }
    }

    public void addReviewReply(ReviewReply reviewReply) {
        this.reviewReplyList.add(reviewReply);

        if (reviewReply.getReview() != this) {
            reviewReply.setReview(this);
        }
    }

    /* 정적 생성 메서드 */
    public static Review createReview (
            Member member,
            String title,
            String body,
            int starPoint,
            String target_hospital
    ) {
        Review review = getReview(member, title, body, starPoint, target_hospital);

        return review;
    }

    public static Review createReview (
            Member member,
            String title,
            String body,
            String reviewPhotoPath,
            int starPoint,
            String target_hospital
    ) {
        Review review = getReview(member, title, body, starPoint, target_hospital);
        review.reviewPhotoPath = reviewPhotoPath;
        return review;
    }

    private static Review getReview(Member member, String title, String body, int starPoint, String target_hospital) {
        if (starPoint <= 0 || starPoint > 5) {
            throw new IllegalValueException("별점은 1점부터 5점까지만 작성 가능합니다.");
        }

        Review review = new Review();
        review.setMember(member);
        review.title = title;
        review.body = body;
        review.status = ReviewStatus.WRITE;
        review.createAt = LocalDateTime.now();
        review.target_hospital = target_hospital;
        review.starPoint = starPoint;
        return review;
    }

    /**
     * 리뷰 삭제
     */
    public void delete() {

        if (this.status == ReviewStatus.DELETE) {
            throw new IllegalStateException("이미 삭제된 리뷰입니다.");
        }

        this.status = ReviewStatus.DELETE;
    }
}
