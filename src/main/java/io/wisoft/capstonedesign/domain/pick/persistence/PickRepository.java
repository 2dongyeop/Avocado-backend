package io.wisoft.capstonedesign.domain.pick.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PickRepository {

    private final EntityManager em;

    /**
     * 찜하기
     */
    public void save(final Pick pick) { em.persist(pick); }

    /**
     * 찜하기 단건 조회
     */
    public Optional<Pick> findOne(final Long pickId) {
        return Optional.ofNullable(em.find(Pick.class, pickId));
    }

    /**
     * 특정 작성자의 찜한 목록 조회
     */
    public List<Pick> findByMemberId(final Long memberId) {

        return em.createQuery("select p from Pick p join fetch p.member m where m.id = :id", Pick.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 작성자의 찜한 목록 내림차순 조회
     */
    public List<Pick> findByMemberIdOrderByCreateDesc(final Long memberId) {

        return em.createQuery("select p from Pick p join fetch p.member m where m.id = :id order by p.createAt desc", Pick.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 작성자의 찜한 목록 오름차순 조회 - 기본 세팅!
     */
    public List<Pick> findByMemberIdOrderByCreateAsc(final Long memberId) {

        return em.createQuery("select p from Pick p join fetch p.member m where m.id = :id order by p.createAt asc", Pick.class)
                .setParameter("id", memberId)
                .getResultList();
    }
}
