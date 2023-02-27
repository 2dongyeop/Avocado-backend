package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 리뷰댓글
 * 한 게시글에 달린 여러 댓글들을 관변
 */
@Entity
@Getter
public class ReviewReply {

    @Id @GeneratedValue()
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
    public void setMember(Member member) {
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

    public void setReview(Review review) {
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
}
