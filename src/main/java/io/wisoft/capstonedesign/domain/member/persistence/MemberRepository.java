package io.wisoft.capstonedesign.domain.member.persistence;

import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /*
     * 회원가입 : signUp
     */
    public void signUp(final Member member) {
        em.persist(member);
    }

    /*
     * 로그인 : login
     */
    public void login(final Member member) {

    }


    /* 의료진 탈퇴 */
    public void deleteMember(final Long memberId) {
        Member member = findOne(memberId).orElseThrow(NullMemberException::new);
        em.remove(member);
    }

    /*
     * 회원조회
     */
    public Optional<Member> findOne(final Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByNickname(final String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    public List<Member> findByEmail(final String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }
}
