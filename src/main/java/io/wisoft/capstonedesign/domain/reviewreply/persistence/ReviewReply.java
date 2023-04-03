package io.wisoft.capstonedesign.domain.reviewreply.persistence;

import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.global.enumeration.status.ReviewReplyStatus;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리뷰댓글
 * 한 게시글에 달린 여러 댓글들을 관변
 */
@Entity
@Getter
@NoArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "createAt", column = @Column(name = "review_reply_create_at", nullable = false)),
        @AttributeOverride(name = "updateAt", column = @Column(name = "review_reply_update_at"))
})
public class ReviewReply extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_reply_id")
    private Long id;

    @Column(name = "reply", nullable = false)
    private String reply;

    @Column(name = "review_reply_status")
    @Enumerated(EnumType.STRING)
    private ReviewReplyStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /* 연관관계 메서드 */
    public void setMember(final Member member) {
        //comment: 기존 관계 제거
        if (this.member != null) {
            this.member.getReviewReplyList().remove(this);
        }

        this.member = member;

        //comment: 무한루프에 빠지지 않도록 체크
        if (!member.getReviewReplyList().contains(this)) {
            member.getReviewReplyList().add(this);
        }
    }

    public void setReview(final Review review) {
        //comment: 기존 관계 제거
        if (this.review != null) {
            this.review.getReviewReplyList().remove(this);
        }

        this.review = review;

        //comment: 무한루프에 빠지지 않도록 체크
        if (!review.getReviewReplyList().contains(this)) {
            review.getReviewReplyList().add(this);
        }
    }

    /* 정적 생성 메서드 */
    @Builder
    public static ReviewReply createReviewReply(
            final Member member,
            final Review review,
            final String reply) {

        ReviewReply reviewReply = new ReviewReply();

        reviewReply.setMember(member);
        reviewReply.setReview(review);
        reviewReply.reply = reply;

        reviewReply.createEntity();
        reviewReply.status = ReviewReplyStatus.WRITE.WRITE;

        return reviewReply;
    }

    /**
     * 리뷰댓글 삭제
     */
    public void delete() {

        if (this.status == ReviewReplyStatus.DELETE.DELETE) {
            throw new IllegalStateException("이미 삭제된 댓글입니다.");
        }

        this.status = ReviewReplyStatus.DELETE.DELETE;
    }


    /**
     * 리뷰댓글 수정
     */
    public void updateReply(final String reply) {

        this.reply = reply;
        this.updateEntity();
    }
}
