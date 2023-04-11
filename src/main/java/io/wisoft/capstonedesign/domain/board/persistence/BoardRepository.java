package io.wisoft.capstonedesign.domain.board.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    /**
     * 게시글 단건 상세 조회
     */
    @Query("select b from Board b" +
            " join fetch b.member m" +
            " join b.boardReplyList br" +
            " where b.id =:id")
    Optional<Board> findDetailById(@Param("id") final Long id);


    /**
     * 게시글 목록을 페이징 조회
     */
    @Query(value = "select b from Board b where b.status = 'WRITE'", countQuery = "select count(b) from Board b")
    Page<Board> findAllUsingPaging(final Pageable pageable);

    @Query("select b from Board b join fetch b.member m")
    List<Board> findAllByMember();
}
