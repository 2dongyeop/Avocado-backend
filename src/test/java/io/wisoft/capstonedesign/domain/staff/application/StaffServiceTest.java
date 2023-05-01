package io.wisoft.capstonedesign.domain.staff.application;

import io.wisoft.capstonedesign.domain.auth.application.AuthService;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthentication;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffHospitalRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPhotoPathRequest;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StaffServiceTest {

    @Autowired EntityManager em;
    @Autowired StaffService staffService;
    @Autowired AuthService authService;
    @Autowired EncryptHelper encryptHelper;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;


    @Test
    public void 의료진_단건_조회() throws Exception {
        //given -- 조건
        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email("email1")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email1")
                .code("ssss")
                .build());

        final Long signUpId = authService.signUpStaff(request);


        //when -- 동작
        final Staff staff = staffService.findById(signUpId);

        //then -- 검증
        Assertions.assertThat(staff.getEmail()).isEqualTo("email1");
    }


    @Test
    public void 의료진_단건_조회_실패() throws Exception {
        //given -- 조건
        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email("email1")
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .code("ssss")
                .build();

        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("email1")
                .code("ssss")
                .build());

        final Long signUpId = authService.signUpStaff(request);


        //when -- 동작
        //then -- 검증
        assertThrows(NullStaffException.class, () -> {
            Staff staff = staffService.findById(100L);
        });
    }

    @Test
    public void 의료진_비밀번호_수정() throws Exception {

        //given -- 조건

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL", "ssss");
        mailAuthenticationRepository.save(MailAuthentication.builder()
                .email("ldy_1204@naver.com")
                .code("ssss")
                .build());

        final Long id = authService.signUpStaff(request1);
        final Staff staff = staffService.findById(id);

        //when -- 동작
        final UpdateStaffPasswordRequest request2 = new UpdateStaffPasswordRequest("1111", "2222");

        staffService.updatePassword(staff.getId(), request2);

        //then -- 검증
        final Staff updateStaff = staffService.findById(id);
        Assertions.assertThat(staff.getPassword()).isEqualTo(updateStaff.getPassword());
    }

    @Test
    public void 의료진_비밀번호_수정_실패() throws Exception {

        //given -- 조건
        final MailAuthentication mail = mailAuthenticationRepository.save(
                MailAuthentication.builder()
                        .email("ldy_1112@naver.com")
                        .code("ssss")
                        .build());

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1112@naver.com", "1111", "1111","hhhh", "DENTAL", "ssss");
        final Long id = authService.signUpStaff(request1);
        final Staff staff = staffService.findById(id);

        //when -- 동작
        final UpdateStaffPasswordRequest request2 = new UpdateStaffPasswordRequest("0000", "2222");

        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            staffService.updatePassword(staff.getId(), request2);
        });
    }

    @Test
    public void 의료진_프로필사진_수정() throws Exception {

        //given -- 조건

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL", "ssss");
        mailAuthenticationRepository.save(
                MailAuthentication.builder()
                        .email("ldy_1204@naver.com")
                        .code("ssss")
                        .build());

        final Long id = authService.signUpStaff(request1);
        final Staff staff = staffService.findById(id);

        //when -- 동작
        final String newPhotoPath = "새로운사진경로";
        final UpdateStaffPhotoPathRequest request2 = new UpdateStaffPhotoPathRequest(newPhotoPath);
        staffService.uploadPhotoPath(staff.getId(), request2);

        //then -- 검증
        Assertions.assertThat(staff.getStaffPhotoPath()).isEqualTo(newPhotoPath);
    }

    @Test
    public void 의료진_병원_수정() throws Exception {

        //given -- 조건
        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL", "ssss");
        mailAuthenticationRepository.save(
                MailAuthentication.builder()
                        .email("ldy_1204@naver.com")
                        .code("ssss")
                        .build());

        final Long id = authService.signUpStaff(request1);

        //when -- 동작
        final UpdateStaffHospitalRequest request2 = new UpdateStaffHospitalRequest("서울대병원");
        staffService.updateStaffHospital(id, request2);

        //then -- 검증
        final Staff getStaff = staffService.findById(id);
        Assertions.assertThat(getStaff.getHospital().getName()).isEqualTo("서울대병원");
    }

    @Test
    public void 의료진_병원_수정_실패() throws Exception {

        //given -- 조건
        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL", "ssss");
        mailAuthenticationRepository.save(
                MailAuthentication.builder()
                        .email("ldy_1204@naver.com")
                        .code("ssss")
                        .build());

        final Long id = authService.signUpStaff(request1);
        final Staff staff = staffService.findById(id);

        //when -- 동작
        final UpdateStaffHospitalRequest request2 = new UpdateStaffHospitalRequest("dsadasda");

        //then -- 검증
        assertThrows(NullHospitalException.class, () -> {
            staffService.updateStaffHospital(staff.getId(), request2);
        });
    }

    @Test
    public void 의료진_탈퇴() throws Exception {

        //given -- 조건
        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        final CreateStaffRequest request1 = new CreateStaffRequest(hospital.getId(), "lee1", "ldy_1204@naver.com", "1111", "1111","hhhh", "DENTAL", "ssss");
        mailAuthenticationRepository.save(
                MailAuthentication.builder()
                        .email("ldy_1204@naver.com")
                        .code("ssss")
                        .build());
        final Long id = authService.signUpStaff(request1);

        //when -- 동작
        staffService.deleteStaff(id);

        //then -- 검증
        assertThrows(NullStaffException.class, () -> {
            Staff staff = staffService.findById(id);
        });
    }
}