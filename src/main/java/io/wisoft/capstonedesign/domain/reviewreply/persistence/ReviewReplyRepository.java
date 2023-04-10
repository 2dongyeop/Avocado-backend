package io.wisoft.capstonedesign.domain.reviewreply.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {


    /**
     * 특정 리뷰의 댓글 목록 조회
     */
    @Query("select rr from ReviewReply rr join fetch rr.review r where r.id = :id")
    List<ReviewReply> findByReviewId(@Param("id") final Long reviewId);

    /**
     * 특정 리뷰의 댓글 목록 오름차순 조회
     */
    @Query("select rr from ReviewReply rr join fetch rr.review r where r.id = :id order by rr.createdAt asc")
    List<ReviewReply> findAllOrderByCreatedAtAsc(@Param("id") final Long id);

    /**
     * 특정 리뷰의 댓글 목록 내림차순 조회
     */
    @Query("select rr from ReviewReply rr join fetch rr.review r where r.id = :id order by rr.createdAt desc")
    List<ReviewReply> findAllOrderByCreatedAtDesc(@Param("id") final Long id);

    /** 상세 조회 */
    @Query("select rr from ReviewReply rr" +
            " join fetch rr.review r" +
            " join fetch r.member rm" +
            " join fetch rr.member m")
    Optional<ReviewReply> findDetailById(@Param("id") final Long reviewReplyId);
}
