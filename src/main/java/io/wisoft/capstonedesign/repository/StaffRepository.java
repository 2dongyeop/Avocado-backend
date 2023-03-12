package io.wisoft.capstonedesign.repository;

import io.wisoft.capstonedesign.domain.Board;
import io.wisoft.capstonedesign.domain.BoardReply;
import io.wisoft.capstonedesign.domain.Review;
import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.exception.nullcheck.NullStaffException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StaffRepository {

    @PersistenceContext
    private final EntityManager em;

    /**
     * 의료진 가입
     */
    public void signUp(Staff staff) {
        em.persist(staff);
    }

    /**
     * 로그인
     */
    //TODO : 의료진 로그인 구현하기
    public void login(Staff staff) {

    }

    /**
     * 자신이 속한 병원의 리뷰 목록 조회하기
     */
    public List<Review> findReviewListByStaffHospitalName(String targetHospital) {

        return em.createQuery("select r from Review r where r.target_hospital = :targetHospital", Review.class)
                .setParameter("targetHospital", targetHospital)
                .getResultList();
    }

    /**
     * 자신이 댓글 단 게시글 목록 조회
     */
    public List<Board> findBoardListByStaffId(Long staffId) {

        List<BoardReply> boardReplyList = em.createQuery("select br from BoardReply br join br.staff s where s.id = :id", BoardReply.class)
                .setParameter("id", staffId)
                .getResultList();

        return boardReplyList.stream()
                .map(BoardReply::getBoard)
                .collect(Collectors.toList());
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
