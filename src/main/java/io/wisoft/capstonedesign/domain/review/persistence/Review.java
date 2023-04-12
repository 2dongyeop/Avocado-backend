package io.wisoft.capstonedesign.domain.review.persistence;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.global.enumeration.status.ReviewStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "review_title", nullable = false)
    private String title;

    //pg 사용시 @Lob 지우고, @Column(nullable = false, columnDefinition="TEXT")로 바꾸기
    @Lob
    @Column(name = "review_body", nullable = false, columnDefinition="TEXT")
    private String body;

    @Column(name = "review_photo_path")
    private String reviewPhotoPath;

    @Column(name = "star_point", nullable = false)
    private int starPoint;  //TODO: 1~5의 정수만 들어가도록 유효성 검사 추가요망

    @Column(name = "target_hospital", nullable = false)
    private String targetHospital;

    @Column(name = "review_status")
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewReply> reviewReplyList = new ArrayList<>();

    /* 연관관계 편의 메서드 */
    public void setMember(final Member member) {
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

    /* 정적 생성 메서드 */
    @Builder
    public static Review createReview (
            final Member member,
            final String title,
            final String body,
            final String reviewPhotoPath,
            final int starPoint,
            final String target_hospital) {

        Review review = new Review();
        review.setMember(member);
        review.title = title;
        review.body = body;
        review.targetHospital = target_hospital;
        review.starPoint = starPoint;
        review.reviewPhotoPath = reviewPhotoPath;

        review.status = ReviewStatus.WRITE;
        review.createEntity();

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
        this.updateEntity();
    }

    /**
     * 리뷰 제목 및 본문 수정
     */
    public void updateTitleBody(final String newTitle, final String newBody) {

        this.title = newTitle;
        this.body = newBody;
        this.updateEntity();
    }
}
