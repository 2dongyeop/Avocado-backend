package io.wisoft.capstonedesign.domain.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByNickname(final String nickname);

    List<Member> findByEmail(final String email);

    Optional<Member> findByNickname(final String nickname);
}
