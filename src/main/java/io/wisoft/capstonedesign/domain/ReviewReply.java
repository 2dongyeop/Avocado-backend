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
}
