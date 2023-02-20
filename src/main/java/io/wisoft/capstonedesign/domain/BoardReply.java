package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 게시글에 대한 댓글
 * 한 게시글에는 여러 댓글들이 달릴 수 있다.
 */
@Entity
@Getter
public class BoardReply {

    @Id @GeneratedValue
    @Column(name = "board_reply_id")
    private Long id;

    @Column(name = "reply", nullable = false)
    private String reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;
}
