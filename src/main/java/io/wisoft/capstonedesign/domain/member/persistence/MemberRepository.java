package io.wisoft.capstonedesign.domain.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 회원 상세 조회
     */
    @Query("select m from Member m" +
            " join m.boardList b" +
            " join m.reviewList r" +
            " join m.reviewReplyList rr" +
            " join m.appointmentList a" +
            " join m.pickList p" +
            " where m.id = :id")
    Optional<Member> findDetailById(@Param("id") final Long id);

    Optional<Member> findByEmail(final String email);
    List<Member> findValidateMemberByNickname(final String nickname);
    List<Member> findValidateMemberByEmail(final String email);
}
