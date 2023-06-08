package io.wisoft.capstonedesign.domain.reviewreply.persistence;

import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * 리뷰댓글
 * 한 게시글에 달린 여러 댓글들을 관변
 */
@Entity
@Getter
@NoArgsConstructor
public class ReviewReply extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_reply_id")
    private Long id;

    @Column(name = "reply", nullable = false)
    private String reply;

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

        validateParam(member, review, reply);

        final ReviewReply reviewReply = new ReviewReply();
        reviewReply.setMember(member);
        reviewReply.setReview(review);
        reviewReply.reply = reply;

        reviewReply.createEntity();
        return reviewReply;
    }

    private static void validateParam(final Member member, final Review review, final String reply) {
        Assert.notNull(member, "member는 필수입니다.");
        Assert.notNull(review, "review는 필수입니다.");
        Assert.hasText(reply, "review는 필수입니다.");
    }

    /**
     * 리뷰댓글 수정
     */
    public void updateReply(final String reply) {

        this.reply = reply;
        this.updateEntity();
    }
}
