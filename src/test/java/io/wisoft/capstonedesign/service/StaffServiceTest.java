package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.board.Board;
import io.wisoft.capstonedesign.review.Review;
import io.wisoft.capstonedesign.boardreply.BoardReply;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.enumeration.status.StaffStatus;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateStaffException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import io.wisoft.capstonedesign.hospital.Hospital;
import io.wisoft.capstonedesign.member.Member;
import io.wisoft.capstonedesign.staff.StaffRepository;
import io.wisoft.capstonedesign.staff.Staff;
import io.wisoft.capstonedesign.staff.StaffService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StaffServiceTest {

    @Autowired EntityManager em;
    @Autowired
    StaffService staffService;
    @Autowired StaffRepository staffRepository;

    @Test(expected = DuplicateStaffException.class)
    public void 의료진중복검증() throws Exception {
        //given -- 조건
        Hospital hospital = Hospital.createHospital("아보카도병원", "04200000000", "대전 유성구", "365일 연중무휴");
        em.persist(hospital);

        HospitalDept hospitalDept = HospitalDept.OBSTETRICS;

        //when -- 동작
        staffService.signUp(hospital.getId(), "lee", "ldy_1204@naver.com", "1111", "hhhh", hospitalDept);
        staffService.signUp(hospital.getId(), "dong", "ldy_1204@naver.com", "1111", "hhhh", hospitalDept);

        //then -- 검증
        fail("의료진의 이메일이 중복되어 예외가 발생해야 한다.");
    }

    @Test(expected = NullStaffException.class)
    public void 의료진_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Staff staff = staffService.findOne(100L);

        //then -- 검증
        fail("해당 staffId에 일치하는 의료진 정보가 없어 예외가 발생해야 한다.");
    }

    @Test
    public void 자신이_속한_병원의_리뷰_목록_조회() throws Exception {
        //given -- 조건

        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Member member = Member.newInstance("lee", "ldy@naver.com", "1111", "0000");
        em.persist(member);

        Review review = Review.createReview(member, "good", "good hospital", 5, "아보카도");
        em.persist(review);

        Staff staff = Staff.newInstance(hospital, "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        em.persist(staff);

        //when -- 동작
        List<Review> reviewListByHospitalName = staffService.findReviewByStaffHospitalName(staff.getId());

        //then -- 검증
        Assertions.assertThat(reviewListByHospitalName.size()).isEqualTo(1);
    }

    @Test
    public void 자신이_댓글을_작성한_게시글_목록_조회() throws Exception {
        //given -- 조건

        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Member member = Member.newInstance("lee", "ldy@naver.com", "1111", "0000");
        em.persist(member);

        Staff staff = Staff.newInstance(hospital, "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        em.persist(staff);

        Board board = Board.createBoard(member, "이가 아파요", "치과추천좀", HospitalDept.DENTAL);
        em.persist(board);

        BoardReply boardReply = BoardReply.createBoardReply(board, staff, "세계최고치과로 가세요.");
        em.persist(boardReply);

        //when -- 동작
        List<Board> boardList = staffService.findBoardListByStaffId(staff.getId());

        //then -- 검증
        Assertions.assertThat(boardList.size()).isEqualTo(1);
    }

    @Test
    public void 의료진_비밀번호_수정() throws Exception {

        //given -- 조건
        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Staff staff = Staff.newInstance(hospital, "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        em.persist(staff);

        //when -- 동작
        staffService.updatePassword(staff.getId(), "1111", "2222");

        //then -- 검증
        Assertions.assertThat(staff.getPassword()).isEqualTo("2222");
    }

    @Test(expected = IllegalValueException.class)
    public void 의료진_비밀번호_수정_실패() throws Exception {

        //given -- 조건
        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Staff staff = Staff.newInstance(hospital, "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        em.persist(staff);

        //when -- 동작
        staffService.updatePassword(staff.getId(), "1133", "2222");

        //then -- 검증
        fail("기존 의료진 비밀번호가 일치하지 않아 예외가 발생해야 한다.");
    }
    
    @Test
    public void 의료진_프로필사진_수정() throws Exception {

        //given -- 조건
        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Staff staff = Staff.newInstance(hospital, "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        em.persist(staff);

        //when -- 동작
        String newPhotoPath = "새로운사진경로";
        staffService.uploadPhotoPath(staff.getId(), newPhotoPath);

        //then -- 검증
        Assertions.assertThat(staff.getStaffPhotoPath()).isEqualTo(newPhotoPath);
    }

    @Test
    public void 의료진_병원_수정() throws Exception {

        //given -- 조건
        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Staff staff = Staff.newInstance(hospital, "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        em.persist(staff);

        //when -- 동작
        staffService.updateStaffHospital(staff.getId(), "hospital1");

        //then -- 검증
        Staff getStaff = staffService.findOne(staff.getId());
        Assertions.assertThat(getStaff.getHospital().getName()).isEqualTo("hospital1");
    }

    @Test(expected = NullHospitalException.class)
    public void 의료진_병원_수정_실패() throws Exception {

        //given -- 조건
        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Staff staff = Staff.newInstance(hospital, "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        em.persist(staff);

        //when -- 동작
        staffService.updateStaffHospital(staff.getId(), "아보카도111111111");

        //then -- 검증
        fail("존재하지 않는 병원명을 입력해 예외가 발생해야 한다.");
    }

    @Test
    public void 의료진_탈퇴() throws Exception {

        //given -- 조건
        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Long id = staffService.signUp(hospital.getId(), "lee", "1204@naver.com", "1111", "license", HospitalDept.OBSTETRICS);
        Staff staff = staffService.findOne(id);

        //when -- 동작
        staff.delete();

        //then -- 검증
        Assertions.assertThat(staff.getStatus()).isEqualTo(StaffStatus.DELETE);
    }
}