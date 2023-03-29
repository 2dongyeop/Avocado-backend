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
            Member member = Member.builder()
                    .nickname("member1")
                    .email("email1")
                    .password("password1")
                    .phoneNumber("0000")
                    .build();
            em.persist(member);

            Hospital hospital = Hospital.builder()
                    .name("hospital1")
                    .number("042")
                    .address("대전")
                    .operatingTime("연중무휴")
                    .build();
            em.persist(hospital);

            Board board = Board.builder()
                    .member(member)
                    .title("title1")
                    .body("body1")
                    .dept(HospitalDept.DENTAL)
                    .build();
            em.persist(board);

            Appointment appointment = Appointment.builder()
                    .member(member)
                    .hospital(hospital)
                    .dept(HospitalDept.DENTAL)
                    .comment("comment1")
                    .appointName("name1")
                    .appointPhonenumber("phone1")
                    .build();
            em.persist(appointment);


            Staff staff = Staff.newInstance(hospital, "name1", "email1", "password1", "path1", HospitalDept.DENTAL);
            em.persist(staff);

            BoardReply boardReply = BoardReply.builder()
                    .board(board)
                    .staff(staff)
                    .reply("reply1")
                    .build();
            em.persist(boardReply);

            Pick pick = Pick.createPick(member, hospital);
            em.persist(pick);


            Review review = Review.builder()
                    .member(member)
                    .title("title1")
                    .body("body1")
                    .starPoint(4)
                    .target_hospital("hospital1")
                    .build();
            em.persist(review);

            ReviewReply reviewReply = ReviewReply.builder()
                    .member(member)
                    .review(review)
                    .reply("reply1")
                    .build();
            em.persist(reviewReply);

            HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, "path", "title1", HospitalDept.DENTAL);
            em.persist(healthInfo);

            BusInfo busInfo = BusInfo.createBusInfo("path", BusArea.DAEJEON);
            em.persist(busInfo);
        }

        public void dbInit2() {
            Member member = Member.builder()
                    .nickname("member2")
                    .email("email2")
                    .password("password2")
                    .phoneNumber("0011")
                    .build();
            em.persist(member);

            Hospital hospital = Hospital.builder()
                    .name("hospital2")
                    .number("041")
                    .address("대전2")
                    .operatingTime("연중무휴")
                    .build();
            em.persist(hospital);

            Board board = Board.builder()
                    .member(member)
                    .title("title2")
                    .body("body2")
                    .dept(HospitalDept.DENTAL)
                    .build();
            em.persist(board);

            Appointment appointment = Appointment.builder()
                    .member(member)
                    .hospital(hospital)
                    .dept(HospitalDept.DENTAL)
                    .comment("comment2")
                    .appointName("name2")
                    .appointPhonenumber("phone2")
                    .build();
            em.persist(appointment);

            Staff staff = Staff.newInstance(hospital, "name2", "email2", "password1", "path1", HospitalDept.DENTAL);
            em.persist(staff);

            BoardReply boardReply = BoardReply.builder()
                    .board(board)
                    .staff(staff)
                    .reply("reply2")
                    .build();
            em.persist(boardReply);

            Pick pick = Pick.createPick(member, hospital);
            em.persist(pick);

            Review review = Review.builder()
                    .member(member)
                    .title("title2")
                    .body("body2")
                    .starPoint(4)
                    .target_hospital("hospital2")
                    .build();
            em.persist(review);

            ReviewReply reviewReply = ReviewReply.builder()
                    .member(member)
                    .review(review)
                    .reply("reply2")
                    .build();
            em.persist(reviewReply);

            HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, "path", "title2", HospitalDept.DENTAL);
            em.persist(healthInfo);

            BusInfo busInfo = BusInfo.createBusInfo("path", BusArea.DAEJEON);
            em.persist(busInfo);
        }
    }
}
