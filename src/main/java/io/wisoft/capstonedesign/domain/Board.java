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

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Column(name = "board_body", nullable = false)
    private String body;

    @CreatedDate
    @Column(name = "board_createAt", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "board_updateAt", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "board_photo_path", nullable = false)
    private String boardPhotoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private final List<BoardReply> boardReplyList = new ArrayList<>();
}
