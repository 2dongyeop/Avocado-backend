package io.wisoft.capstonedesign.domain.staff.application;

import io.wisoft.capstonedesign.domain.auth.application.AuthService;
import io.wisoft.capstonedesign.domain.auth.application.EmailServiceImpl;
import io.wisoft.capstonedesign.domain.auth.persistence.MailAuthenticationRepository;
import io.wisoft.capstonedesign.domain.auth.web.dto.CertificateMailRequest;
import io.wisoft.capstonedesign.domain.auth.web.dto.CreateStaffRequest;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.web.dto.UpdateStaffPasswordRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StaffServiceTest {

    @Autowired EntityManager em;
    @Autowired StaffService staffService;
    @Autowired AuthService authService;
    @Autowired EmailServiceImpl emailService;
    @Autowired MailAuthenticationRepository mailAuthenticationRepository;


    @Test
    public void 의료진_단건_조회() throws Exception {
        //given -- 조건
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        final Staff staff = staffService.findById(signUpId);

        //then -- 검증
        Assertions.assertThat(staff.getEmail()).isEqualTo(email);
    }


    @Test
    public void 의료진_단건_조회_실패() throws Exception {
        //given -- 조건
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

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
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);
        final Staff staff = staffService.findById(signUpId);

        //when -- 동작
        final UpdateStaffPasswordRequest request2 = new UpdateStaffPasswordRequest("password", "newPassword");

        staffService.updatePassword(staff.getId(), request2);

        //then -- 검증
        final Staff updateStaff = staffService.findById(signUpId);
        Assertions.assertThat(staff.getPassword()).isEqualTo(updateStaff.getPassword());
    }

    @Test
    public void 의료진_비밀번호_수정_실패() throws Exception {

        //given -- 조건
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        //when -- 동작
        final Long signUpId = authService.signUpStaff(request);
        final Staff staff = staffService.findById(signUpId);

        //when -- 동작
        final UpdateStaffPasswordRequest request2 = new UpdateStaffPasswordRequest("0000", "2222");

        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            staffService.updatePassword(staff.getId(), request2);
        });
    }

    @Test
    public void updateStaff_success() throws Exception {

        //given -- 조건

        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final Long signUpId = authService.signUpStaff(request);
        final Staff staff = staffService.findById(signUpId);

        //when -- 동작
        final String newHospital = "";
        final String newPhotoPath = "newPhotoPath";

        staffService.updateStaff(staff.getId(), newHospital, newPhotoPath);

        //then -- 검증
        Assertions.assertThat(staff.getHospital().getName()).isNotEqualTo(newHospital);
        Assertions.assertThat(staff.getStaffPhotoPath()).isEqualTo(newPhotoPath);
    }


    @Test
    public void 의료진_탈퇴() throws Exception {

        //given -- 조건
        final String email = "email@naver.com";

        //이메일 인증 코드 보내기
        final String code = emailService.sendCertificationCode(email);

        //이메일 인증
        final CertificateMailRequest mailRequest = new CertificateMailRequest(email, code);
        emailService.certificateEmail(mailRequest);

        //병원 생성
        final Hospital hospital = Hospital.builder()
                .name("name1")
                .number("number1")
                .address("address1")
                .operatingTime("oper1")
                .build();
        em.persist(hospital);

        //의료진 가입 요청
        final CreateStaffRequest request = CreateStaffRequest.builder()
                .hospitalId(hospital.getId())
                .name("staff1")
                .email(email)
                .password1("password")
                .password2("password")
                .licensePath("license")
                .dept("DENTAL")
                .build();

        final Long signUpId = authService.signUpStaff(request);

        //when -- 동작
        staffService.deleteStaff(signUpId);

        //then -- 검증
        assertThrows(NullStaffException.class, () -> {
            staffService.findById(signUpId);
        });
    }
}