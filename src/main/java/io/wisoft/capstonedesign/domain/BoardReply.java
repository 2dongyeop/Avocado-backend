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

    @Id @GeneratedValue()
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

    /* 연관관계 편의메서드 */
    public void setBoard(Board board) {
        //comment: 기존 관계 제거
        if (this.board != null) {
            this.board.getBoardReplyList().remove(this);
        }

        this.board = board;

        //comment: 무한루프 방지
        if (!board.getBoardReplyList().contains(this)) {
            board.getBoardReplyList().add(this);
        }
    }

    public void setStaff(Staff staff) {
        //comment: 기존 관계 제거
        if (this.staff != null) {
            this.staff.getBoardReplyList().remove(this);
        }

        this.staff = staff;

        //comment: 무한루프에 빠지지 않도록 체크
        if (!staff.getBoardReplyList().contains(this)) {
            staff.getBoardReplyList().add(this);
        }
    }
}
