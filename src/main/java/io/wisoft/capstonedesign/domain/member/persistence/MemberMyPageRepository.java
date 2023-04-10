package io.wisoft.capstonedesign.domain.member.persistence;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMyPageRepository extends JpaRepository<Member, Long> {


    /**
     * 자신이 쓴 리뷰 목록 페이징 조회
     */
    @Query(value = "select r from Review r" +
            " join fetch r.member m" +
            " where m.id =:id"
            , countQuery = "select count(r) from Review r")
    Page<Review> findReviewsByMemberIdUsingPaging(@Param("id") final Long memberId, final Pageable pageable);


    /**
     * 자신이 쓴 게시글 목록 페이징 조회
     */
    @Query(value = "select b from Board b" +
            " join fetch b.member m" +
            " where m.id = :id",
            countQuery = "select count(b) from Board b")
    Page<Board> findBoardsByMemberIdUsingPaging(@Param("id") final Long memberId, final Pageable pageable);


    /**
     * 자신의 병원 예약 목록 조회
     */
    @Query("select a from Appointment a join fetch a.member m where m.id = :id")
    List<Appointment> findAppointmentsByMemberId(@Param("id") final Long id);


    /**
     * 자신이 찜한 병원 목록 조회
     */
    @Query(value = "select p from Pick p" +
            " join fetch p.member m" +
            " join fetch p.hospital h" +
            " where m.id = :id",
            countQuery = "select count(p) from Pick p")
    Page<Pick> findPicksByMemberIdUsingPaging(@Param("id") final Long memberId, final Pageable pageable);
}

