package io.wisoft.capstonedesign.domain.staff.application;

import io.wisoft.capstonedesign.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.auth.application.AuthService;
import io.wisoft.capstonedesign.domain.staff.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffHospitalRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPhotoPathRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StaffServiceTest {

    @Autowired EntityManager em;
    @Autowired StaffService staffService;
    @Autowired AuthService authService;
    @Autowired EncryptHelper encryptHelper;

    @Test(expected = NullStaffException.class)
    public void 의료진_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        Staff staff = staffService.findById(100L);

        //then -- 검증
        fail("해당 staffId에 일치하는 의료진 정보가 없어 예외가 발생해야 한다.");
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

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL");
        Long id = authService.signUpStaff(request1);
        Staff staff = staffService.findById(id);

        //when -- 동작
        UpdateStaffPasswordRequest request2 = new UpdateStaffPasswordRequest("1111", "2222");

        staffService.updatePassword(staff.getId(), request2);

        //then -- 검증
        Staff updateStaff = staffService.findById(id);
        Assertions.assertThat(staff.getPassword()).isEqualTo(updateStaff.getPassword());
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

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL");
        Long id = authService.signUpStaff(request1);
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

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL");
        Long id = authService.signUpStaff(request1);
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

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL");
        Long id = authService.signUpStaff(request1);

        //when -- 동작
        UpdateStaffHospitalRequest request2 = new UpdateStaffHospitalRequest("서울대병원");
        staffService.updateStaffHospital(id, request2);

        //then -- 검증
        Staff getStaff = staffService.findById(id);
        Assertions.assertThat(getStaff.getHospital().getName()).isEqualTo("서울대병원");
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

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL");
        Long id = authService.signUpStaff(request1);
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

        CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL");
        Long id = authService.signUpStaff(request1);

        //when -- 동작
        staffService.deleteStaff(id);

        //then -- 검증
        Staff staff = staffService.findById(id);
        fail("해당 의료진은 탈퇴를 했으므로 예외가 발생해야 한다.");
    }
}