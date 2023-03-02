package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Staff;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StaffRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 사용자 회원 가입
     */
    public void signUp(Staff staff) { em.persist(staff); }

    /**
     * 로그인
     */
    //TODO : 의료진 로그인 구현하기
    public void login(Staff staff) {

    }

    /**
     * 의료진 조회
     */
    public Staff findOne(Long id) {
        return em.find(Staff.class, id);
    }

    public List<Staff> findAll() {
        return em.createQuery("select s from Staff s", Staff.class)
                .getResultList();
    }

    public List<Staff> findByEmail(String email) {
        return em.createQuery("select s from Staff s where s.email = :email", Staff.class)
                .setParameter("email", email)
                .getResultList();
    }
}
