package io.wisoft.capstonedesign.domain.staff.persistence;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffMyPageRepository extends JpaRepository<Staff, Long> {

    /**
     * 자신이 속한 병원의 리뷰 목록 조회하기
     */
    @Query("select r from Review r join fetch r.member m where r.targetHospital = :targetHospital")
    List<Review> findReviewListByStaffHospitalName(@Param("targetHospital") final String targetHospital);

    /**
     * 자신이 댓글을 작성한 게시글들 불러오기
     */
    @Query("select b from Board b" +
            " join b.boardReplyList br" +
            " join fetch b.member m" +
            " join br.staff s" +
            " where s.id = :id")
    List<Board> findBoardICommentByStaffId(@Param("id") final Long id);
}
