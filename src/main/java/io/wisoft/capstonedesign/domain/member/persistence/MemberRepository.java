package io.wisoft.capstonedesign.domain.member.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /*
     * 회원조회
     */
    public Member findOne(final Long id) {
        return em.find(Member.class, id);
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
