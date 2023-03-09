package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.exception.nullcheck.NullBoardException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    /**
     * 게시글 작성(저장)
     */
    public void save(Board board) {
        em.persist(board);
    }

    /**
     * 게시글 단건 조회
     */
    public Board findOne(Long boardId) {
        return em.find(Board.class, boardId);
    }

    /**
     * 게시글 전체 조회
     */
    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }

    /**
     * 특정 작성자의 게시글 조회
     */
    public List<Board> findByMemberId(Long memberId) {
        Member targetMember = em.find(Member.class, memberId);

        return em.createQuery("select b from Board b where b.member = :targetMember", Board.class)
                .setParameter("targetMember", targetMember)
                .getResultList();
    }
}
