package io.wisoft.capstonedesign.domain.staff.persistence;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {


    /**
     * 자신이 속한 병원의 리뷰 목록 조회하기
     */
    @Query("select r from Review r where r.targetHospital = :targetHospital")
    public List<Review> findReviewListByStaffHospitalName(@Param("targetHospital") final String targetHospital);

    /**
     * 자신이 댓글 단 게시글 목록 조회
     */
    public default List<Board> findBoardListByStaffId(final Long staffId) {
        List<BoardReply> boardReplyList = findBoardReplyListByStaffId(staffId);

        return boardReplyList.stream()
                .map(BoardReply::getBoard)
                .collect(Collectors.toList());
    }

    @Query("select br from BoardReply br join fetch br.staff s where s.id = :id")
    List<BoardReply> findBoardReplyListByStaffId(@Param("id") final Long id);

    @Query("select s from Staff s join fetch s.hospital h")
    public List<Staff> findAllByHospital();
}
