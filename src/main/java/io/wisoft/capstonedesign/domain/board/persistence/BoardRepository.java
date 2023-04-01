package io.wisoft.capstonedesign.domain.board.persistence;

import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    /**
     * 특정 작성자의 게시글 조회
     */
    @Query("select b from Board b join fetch b.member m where m.id = :id")
    public List<Board> findByMemberId(@Param("id") final Long memberId);

    /**
     * 게시글 목록 오름차순 조회
     */
    @Query("select b from Board b order by b.createAt asc")
    public List<Board> findAllOrderByCreateAtAsc();

    /**
     * 게시글 목록 내림차순 조회
     */
    @Query("select b from Board b order by b.createAt desc")
    public List<Board> findAllOrderByCreateAtDesc();

    @Query("select b from Board b join fetch b.member m")
    public List<Board> findAllByMember();

    /** 특정 의료진이 댓글을 단 게시글 목록 조회 */
    public default List<Board> findByStaffReply(final Long staffId) {
        List<BoardReply> boardReplyList = getBoardReplyByStaff(staffId);

        return boardReplyList.stream()
                .map(boardReply -> boardReply.getBoard())
                .collect(Collectors.toList());
    }

    @Query("select br from BoardReply br join fetch br.staff s where s.id = :staffId")
    public List<BoardReply> getBoardReplyByStaff(@Param("staffId") final Long staffId);


}
