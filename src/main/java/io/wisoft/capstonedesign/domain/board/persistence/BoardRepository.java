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
     * 특정 작성자의 게시글 조회
     */
    @Query(value = "select b from Board b" +
            " join fetch b.member m" +
            " where m.id = :id",
            countQuery = "select count(b) from Board b")
    Page<Board> findByMemberIdUsingPaging(@Param("id") final Long memberId, final Pageable pageable);


    /**
     * 게시글 목록을 페이징 조회
     */
    @Query(value = "select b from Board b", countQuery = "select count(b) from Board b")
    Page<Board> findAllUsingPaging(final Pageable pageable);

    @Query("select b from Board b join fetch b.member m")
    List<Board> findAllByMember();
}
