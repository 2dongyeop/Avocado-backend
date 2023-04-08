package io.wisoft.capstonedesign.domain.boardreply.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    /**
     * 게시글 단건 상세 조회
     */
    @Query("select br from BoardReply br" +
            " join fetch br.staff s" +
            " join fetch br.board b" +
            " join fetch b.member m" +
            " where br.id = :id")
    Optional<BoardReply> findDetailById(@Param("id") final Long id);


    /**
     * 특정게시글의 댓글 목록 오름차순 조회
     */
    @Query("select br from BoardReply br order by br.createAt asc")
    List<BoardReply> findByBoardIdOrderByCreateAsc();

    /**
     * 특정게시글의 댓글 목록 내림차순 조회
     */
    @Query("select br from BoardReply br order by br.createAt desc")
    List<BoardReply> findByBoardIdOrderByCreateDesc();
}
