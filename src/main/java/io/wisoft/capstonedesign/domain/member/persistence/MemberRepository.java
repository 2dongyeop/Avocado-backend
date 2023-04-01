package io.wisoft.capstonedesign.domain.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByNickname(final String nickname);

    public List<Member> findByEmail(final String email);
}
