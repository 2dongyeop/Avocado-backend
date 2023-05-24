package io.wisoft.capstonedesign.domain.board.persistence;

import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.global.BaseEntity;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.global.enumeration.status.BoardStatus;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "board_photo_path")
    private String boardPhotoPath;

    @Column(name = "board_status")
    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    @Column(name = "dept")
    @Enumerated(EnumType.STRING)
    private HospitalDept dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private final List<BoardReply> boardReplyList = new ArrayList<>();

    /* 연관관계 편의 메서드 */
    public void setMember(final Member member) {
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
    @Builder
    public static Board createBoard(
            final Member member,
            final String title,
            final String body,
            final HospitalDept dept) {

        Board board = new Board();
        board.setMember(member);
        board.title = title;
        board.body = body;
        board.dept = dept;

        board.status = BoardStatus.WRITE;
        board.createEntity();

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
        this.updateEntity();
    }

    /**
     * 게시글 제목 및 본문 수정
     */
    public void updateTitleBody(final String newTitle, final String newBody) {
        this.title = newTitle;
        this.body = newBody;
        this.updateEntity();
    }
}
