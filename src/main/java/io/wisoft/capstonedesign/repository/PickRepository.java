package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Pick;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PickRepository {

    private final EntityManager em;

    /**
     * 찜하기
     */
    public void save(Pick pick) { em.persist(pick); }

    /**
     * 찜하기 단건 조회
     */
    public Pick findOne(Long pickId) {
        return em.find(Pick.class, pickId);
    }

    /**
     * 특정 작성자의 찜한 목록 조회
     */
    public List<Pick> findByMemberId(Long memberId) {

        return em.createQuery("select p from Pick p join p.member m where m.id = :id", Pick.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 작성자의 찜한 목록 내림차순 조회
     */
    public List<Pick> findByMemberIdDESC(Long memberId) {

        return em.createQuery("select p from Pick p join p.member m where m.id = :id order by p.pickedAt desc", Pick.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    /**
     * 특정 작성자의 찜한 목록 오름차순 조회 - 기본 세팅!
     */
    public List<Pick> findByMemberIdASC(Long memberId) {

        return em.createQuery("select p from Pick p join p.member m where m.id = :id order by p.pickedAt asc", Pick.class)
                .setParameter("id", memberId)
                .getResultList();
    }
}
