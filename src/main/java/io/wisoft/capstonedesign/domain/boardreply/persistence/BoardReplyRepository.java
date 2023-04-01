package io.wisoft.capstonedesign.domain.boardreply.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    /**
     * 특정게시글의 댓글 목록 조회
     */
    @Query("select br from BoardReply br join fetch br.board b where b.id = :id")
    public List<BoardReply> findByBoardId(@Param("id") final Long boardId);


    /**
     * 특정게시글의 댓글 목록 오름차순 조회
     */
    @Query("select br from BoardReply br order by br.createAt asc")
    public List<BoardReply> findByBoardIdOrderByCreateAsc();

    /**
     * 특정게시글의 댓글 목록 내림차순 조회
     */
    @Query("select br from BoardReply br order by br.createAt desc")
    public List<BoardReply> findByBoardIdOrderByCreateDesc();
}
