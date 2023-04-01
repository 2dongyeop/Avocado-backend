package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.boardreply.persistence.BoardReply;
import io.wisoft.capstonedesign.domain.staff.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffHospitalRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPhotoPathRequest;
import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
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
    @Autowired StaffService staffService;

    @Test(expected = AssertionError.class)
    public void 의료진중복검증() throws Exception {
        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        CreateStaffRequest request2 = new CreateStaffRequest(hospital.getId(), "lee2", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");

        //when -- 동작
        staffService.signUp(request1);
        staffService.signUp(request2);

        //then -- 검증
        fail("의료진의 이메일이 중복되어 예외가 발생해야 한다.");
    }

    @Test(expected = NullStaffException.class)
    public void 의료진_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Staff staff = staffService.findById(100L);

        //then -- 검증
        fail("해당 staffId에 일치하는 의료진 정보가 없어 예외가 발생해야 한다.");
    }

    public void 자신이_속한_병원의_리뷰_목록_조회() throws Exception {
        //given -- 조건

        Hospital hospital = Hospital.createHospital("아보카도", "04212345678", "대전", "연중무휴");
        em.persist(hospital);

        Member member = Member.newInstance("lee", "ldy@naver.com", "1111", "0000");
        em.persist(member);

        Review review = Review.builder()
                .member(member)
                .title("good")
                .body("good hospital")
                .starPoint(5)
                .target_hospital("아보카도")
                .build();
        em.persist(review);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);

        //when -- 동작
        List<Review> reviewListByHospitalName = staffService.findReviewByStaffHospitalName(id);

        //then -- 검증
        Assertions.assertThat(reviewListByHospitalName.size()).isEqualTo(1);
    }

    @Test
    public void 자신이_댓글을_작성한_게시글_목록_조회() throws Exception {
        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1123")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //회원 생성
        Member member = Member.builder()
                .nickname("nick1123")
                .email("email1231")
                .password("pass1")
                .phoneNumber("0000")
                .build();
        em.persist(member);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);
        Staff staff = staffService.findById(id);

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

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);
        Staff staff = staffService.findById(id);

        //when -- 동작
        UpdateStaffPasswordRequest request2 = new UpdateStaffPasswordRequest("1111", "2222");

        staffService.updatePassword(staff.getId(), request2);

        //then -- 검증
        Assertions.assertThat(staff.getPassword()).isEqualTo(request2.getNewPassword());
    }

    @Test(expected = IllegalValueException.class)
    public void 의료진_비밀번호_수정_실패() throws Exception {

        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);
        Staff staff = staffService.findById(id);

        //when -- 동작
        UpdateStaffPasswordRequest request2 = new UpdateStaffPasswordRequest("0000", "2222");

        staffService.updatePassword(staff.getId(), request2);

        //then -- 검증
        fail("기존 의료진 비밀번호가 일치하지 않아 예외가 발생해야 한다.");
    }
    
    @Test
    public void 의료진_프로필사진_수정() throws Exception {

        //given -- 조건

        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);
        Staff staff = staffService.findById(id);

        //when -- 동작
        String newPhotoPath = "새로운사진경로";
        UpdateStaffPhotoPathRequest request2 = new UpdateStaffPhotoPathRequest(newPhotoPath);
        staffService.uploadPhotoPath(staff.getId(), request2);

        //then -- 검증
        Assertions.assertThat(staff.getStaffPhotoPath()).isEqualTo(newPhotoPath);
    }

    @Test
    public void 의료진_병원_수정() throws Exception {

        //given -- 조건
        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);
        Staff staff = staffService.findById(id);

        //when -- 동작
        UpdateStaffHospitalRequest request2 = new UpdateStaffHospitalRequest("hospital1");
        staffService.updateStaffHospital(staff.getId(), request2);

        //then -- 검증
        Staff getStaff = staffService.findById(staff.getId());
        Assertions.assertThat(getStaff.getHospital().getName()).isEqualTo("hospital1");
    }

    @Test(expected = NullHospitalException.class)
    public void 의료진_병원_수정_실패() throws Exception {

        //given -- 조건
        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);
        Staff staff = staffService.findById(id);

        //when -- 동작
        UpdateStaffHospitalRequest request2 = new UpdateStaffHospitalRequest("dsadasda");
        staffService.updateStaffHospital(staff.getId(), request2);

        //then -- 검증
        fail("존재하지 않는 병원명을 입력해 예외가 발생해야 한다.");
    }

    @Test(expected = NullStaffException.class)
    public void 의료진_탈퇴() throws Exception {

        //given -- 조건
        //병원 생성
        Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "hhhh", "DENTAL");
        Long id = staffService.signUp(request1);

        //when -- 동작
        staffService.deleteStaff(id);

        //then -- 검증
        Staff staff = staffService.findById(id);
        fail("해당 의료진은 탈퇴를 했으므로 예외가 발생해야 한다.");
    }
}