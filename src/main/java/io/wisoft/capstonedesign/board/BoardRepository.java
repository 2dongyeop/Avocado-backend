package io.wisoft.capstonedesign.board;

import io.wisoft.capstonedesign.boardreply.BoardReply;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    /**
     * 게시글 작성(저장)
     */
    public void save(final Board board) {
        em.persist(board);
    }

    /**
     * 게시글 단건 조회
     */
    public Board findOne(final Long boardId) {
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
    public List<Board> findByMemberId(final Long memberId) {

        return em.createQuery("select b from Board b join fetch b.member m where m.id = :id", Board.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 게시글 목록 오름차순 조회
     */
    public List<Board> findAllOrderByCreateAtAsc() {
        return em.createQuery("select b from Board b order by b.createAt", Board.class)
                .getResultList();
    }

    /**
     * 게시글 목록 내림차순 조회
     */
    public List<Board> findAllOrderByCreateAtDesc() {
        return em.createQuery("select b from Board b order by b.createAt desc", Board.class)
                .getResultList();
    }

    public List<Board> findAllByMember() {
        return em.createQuery("select b from Board b join fetch b.member m", Board.class)
                .getResultList();
    }

    /* 특정 의료진이 댓글을 단 게시글 목록 조회 */
    public List<Board> findByStaffReply(final Long staffId) {
        List<BoardReply> boardReplyList = em.createQuery("select br from BoardReply br join fetch br.staff s where s.id = :staffId", BoardReply.class)
                .setParameter("staffId", staffId)
                .getResultList();

        return boardReplyList.stream()
                .map(boardReply -> boardReply.getBoard())
                .collect(Collectors.toList());
    }
}
