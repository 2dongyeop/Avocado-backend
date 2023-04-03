package io.wisoft.capstonedesign.domain.staff.persistence;

import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {


    /**
     * 자신이 속한 병원의 리뷰 목록 조회하기
     */
    @Query("select r from Review r where r.targetHospital = :targetHospital")
    public List<Review> findReviewListByStaffHospitalName(@Param("targetHospital") final String targetHospital);


    /**
     * 자신이 댓글 단 게시글 목록 조회하기 위해
     * 먼저 자신이 작성한 댓글 목록들을 조회하여 반환
     */
    @Query("select br from BoardReply br join fetch br.staff s where s.id = :id")
    List<BoardReply> findBoardReplyListByStaffId(@Param("id") final Long id);


    @Query("select s from Staff s join fetch s.hospital h")
    public List<Staff> findAllByHospital();
}
