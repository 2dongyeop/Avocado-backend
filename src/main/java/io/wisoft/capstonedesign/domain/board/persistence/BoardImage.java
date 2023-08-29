package io.wisoft.capstonedesign.domain.board.persistence;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String savedUrl;

    private Long boardId;

    public BoardImage(final String savedUrl, final Long boardId) {
        this.savedUrl = savedUrl;
        this.boardId = boardId;
    }
}
