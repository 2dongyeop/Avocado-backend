package io.wisoft.capstonedesign.domain;

import io.wisoft.capstonedesign.domain.enumeration.BoardStatus;
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
public class Board {

    @Id @GeneratedValue()
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    //pg 사용시 @Lob 지우고, @Collumn(nullable = false, columnDefinition="TEXT")로 바꾸기
    @Lob
    @Column(name = "board_body", nullable = false)
    private String body;

    @CreatedDate
    @Column(name = "board_create_at", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "board_update_at")
    private LocalDateTime updateAt;

    @Column(name = "board_photo_path")
    private String boardPhotoPath;

    @Column(name = "board_status")
    @Enumerated(EnumType.STRING)
    private BoardStatus status;

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

    /* 정적 생성 메서드 */
    public static Board createBoard(Member member, String title, String body) {
        Board board = getBoard(member, title, body);

        return board;
    }

    public static Board createBoard(Member member, String title, String body, String boardPhotoPath) {
        Board board = getBoard(member, title, body);
        board.boardPhotoPath = boardPhotoPath;

        return board;
    }

    private static Board getBoard(Member member, String title, String body) {
        Board board = new Board();
        board.setMember(member);
        board.title = title;
        board.body = body;
        board.status = BoardStatus.WRITE;
        board.createAt = LocalDateTime.now();
        return board;
    }

    /**
     * 게시글 삭제
     */
    public void delete() {

        if (this.status == BoardStatus.DELETE) {
            throw new IllegalStateException("이미 삭제된 게시글입니다.");
        }

        this.status = BoardStatus.DELETE;
    }
}
