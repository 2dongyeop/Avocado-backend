package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.BoardReply;
import io.wisoft.capstonedesign.domain.Staff;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardReplyRepository {

    private final EntityManager em;

    /**
     * 게시글댓글 저장
     */
    public void save(final BoardReply boardReply) { em.persist(boardReply); }

    /**
     * 게시글댓글 단건 조회
     */
    public BoardReply findOne(final Long boardReplyId) {
        return em.find(BoardReply.class, boardReplyId);
    }

    /**
     * 특정게시글의 댓글 목록 조회
     */
    public List<BoardReply> findByBoardId(final Long boardId) {

        return em.createQuery("select br from BoardReply br join br.board b where b.id = :id", BoardReply.class)
                .setParameter("id", boardId)
                .getResultList();
    }

    /**
     * 특정게시글의 댓글 목록 오름차순 조회
     */
    public List<BoardReply> findByBoardIdOrderByCreateAsc() {

        return em.createQuery("select br from BoardReply br order by br.createAt", BoardReply.class)
                .getResultList();
    }

    /**
     * 특정게시글의 댓글 목록 내림차순 조회
     */
    public List<BoardReply> findByBoardIdOrderByCreateDesc() {

        return em.createQuery("select br from BoardReply br order by br.createAt desc ", BoardReply.class)
                .getResultList();
    }
}
