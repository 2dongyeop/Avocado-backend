package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Board {

    @Id @GeneratedValue()
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_body", nullable = false)
    private String body;

    @CreatedDate
    @Column(name = "board_create_at", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "board_update_at", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "board_photo_path", nullable = false)
    private String boardPhotoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private final List<BoardReply> boardReplyList = new ArrayList<>();

    /* 연관관계 편의 메서드 */
    public void addBoardReply(BoardReply boardReply) {
        this.boardReplyList.add(boardReply);
        if (boardReply.getBoard() != this) { //무한루프에 빠지지 않도록 체크
            boardReply.setBoard(this);
        }
    }

    public void setMember(Member member) {
        //comment: 기존 관계 제거
        if (this.member != null) {
            this.member.getBoardList().remove(this);
        }

        this.member = member;

        //comment: 무한루프 방지
        if (!member.getBoardList().contains(this)) {
            member.getBoardList().add(this);
        }
    }
}
