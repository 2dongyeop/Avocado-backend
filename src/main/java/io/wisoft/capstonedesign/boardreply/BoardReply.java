package io.wisoft.capstonedesign.boardreply;

import io.wisoft.capstonedesign.board.Board;
import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.staff.Staff;
import io.wisoft.capstonedesign.global.enumeration.status.BoardReplyStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverrides({
        @AttributeOverride(name = "createAt", column = @Column(name = "boardreply_create_at", nullable = false)),
        @AttributeOverride(name = "updateAt", column = @Column(name = "boardreply_update_at"))
})
public class BoardReply extends BaseEntity {

    @Id @GeneratedValue()
    @Column(name = "board_reply_id")
    private Long id;

    @Column(name = "reply", nullable = false)
    private String reply;

    @Column(name = "boardreply_status")
    @Enumerated(EnumType.STRING)
    private BoardReplyStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    /* 연관관계 편의메서드 */
    public void setBoard(final Board board) {
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

    public void setStaff(final Staff staff) {
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

    /* 정적 생성 메서드 */
    public static BoardReply createBoardReply(
            final Board board,
            final Staff staff,
            final String reply) {
        BoardReply boardReply = new BoardReply();
        boardReply.setBoard(board);
        boardReply.setStaff(staff);
        boardReply.reply = reply;

        boardReply.createEntity();
        boardReply.status = BoardReplyStatus.WRITE;

        return boardReply;
    }

    /**
     * 댓글 삭제
     */
    public void delete() {

        if (this.status == BoardReplyStatus.DELETE) {
            throw new IllegalStateException("이미 삭제된 댓글입니다.");
        }

        this.status = BoardReplyStatus.DELETE;
    }

    /**
     * 게시글댓글 수정
     */
    public void update(final String reply) {

        this.reply = reply;
        this.updateEntity();
    }
}
