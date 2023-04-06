package io.wisoft.capstonedesign.domain.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(final String email);

    List<Member> findValidateMemberByNickname(final String nickname);
    List<Member> findValidateMemberByEmail(final String email);
}
