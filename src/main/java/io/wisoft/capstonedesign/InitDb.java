package io.wisoft.capstonedesign;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.reviewreply.persistence.ReviewReply;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = Member.newInstance("member1", "email1", "password1", "0000");
            em.persist(member);

            Hospital hospital = Hospital.createHospital("hospital1", "042", "대전", "연중무휴");
            em.persist(hospital);

            Board board = Board.createBoard(member, "title1", "body1", HospitalDept.DENTAL);
            em.persist(board);

            Appointment appointment = Appointment.createAppointment(member, hospital, HospitalDept.DENTAL, "comment1", "name1", "phone1");
            em.persist(appointment);

            Staff staff = Staff.newInstance(hospital, "name1", "email1", "password1", "path1", HospitalDept.DENTAL);
            em.persist(staff);

            BoardReply boardReply = BoardReply.createBoardReply(board, staff, "reply1");
            em.persist(boardReply);

            Pick pick = Pick.createPick(member, hospital);
            em.persist(pick);

            Review review = Review.createReview(member, "title1", "body1", 4, "hospital1");
            em.persist(review);

            ReviewReply reviewReply = ReviewReply.createReviewReply(member, review, "reply1");
            em.persist(reviewReply);

            HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, "path", "title1", HospitalDept.DENTAL);
            em.persist(healthInfo);

            BusInfo busInfo = BusInfo.createBusInfo("path", BusArea.DAEJEON);
            em.persist(busInfo);
        }

        public void dbInit2() {
            Member member = Member.newInstance("member2", "email2", "password1", "0000");
            em.persist(member);

            Hospital hospital = Hospital.createHospital("hospital2", "042", "대전", "연중무휴");
            em.persist(hospital);

            Board board = Board.createBoard(member, "title2", "body2", HospitalDept.DENTAL);
            em.persist(board);

            Appointment appointment = Appointment.createAppointment(member, hospital, HospitalDept.DENTAL, "comment1", "name1", "phone1");
            em.persist(appointment);

            Staff staff = Staff.newInstance(hospital, "name2", "email2", "password1", "path1", HospitalDept.DENTAL);
            em.persist(staff);

            BoardReply boardReply = BoardReply.createBoardReply(board, staff, "reply2");
            em.persist(boardReply);

            Pick pick = Pick.createPick(member, hospital);
            em.persist(pick);

            Review review = Review.createReview(member, "title2", "body2", 4, "hospital2");
            em.persist(review);

            ReviewReply reviewReply = ReviewReply.createReviewReply(member, review, "reply1");
            em.persist(reviewReply);

            HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, "path", "title2", HospitalDept.DENTAL);
            em.persist(healthInfo);

            BusInfo busInfo = BusInfo.createBusInfo("path", BusArea.DAEJEON);
            em.persist(busInfo);
        }
    }
}
