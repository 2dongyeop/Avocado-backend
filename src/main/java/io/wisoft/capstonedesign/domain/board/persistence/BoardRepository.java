package io.wisoft.capstonedesign.domain.board.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    /**
     * 특정 작성자의 게시글 조회
     */
    @Query("select b from Board b join fetch b.member m where m.id = :id")
    List<Board> findByMemberId(@Param("id") final Long memberId);

    /** 게시글 목록을 페이징 후 오름차순으로 조회 */
    @Query(value = "select b from Board b",
            countQuery = "select count(b) from Board b")
    Page<Board> findAllUsingPagingOrderByCreateAtAsc(Pageable pageable);

    /** 게시글 목록을 페이징 후 내림차순으로 조회 */
    @Query(value = "select b from Board b",
            countQuery = "select count(b) from Board b")
    Page<Board> findAllUsingPagingOrderByCreateAtDesc(Pageable pageable);

    @Query("select b from Board b join fetch b.member m")
    List<Board> findAllByMember();
}
