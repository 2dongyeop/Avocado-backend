package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.exception.duplicate.DuplicateStaffException;
import io.wisoft.capstonedesign.exception.nullcheck.NullStaffException;
import io.wisoft.capstonedesign.repository.StaffRepository;
import jakarta.persistence.EntityManager;
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
        Staff staff = staffRepository.findOne(2L);

        //then -- 검증
        fail("해당 staffId에 일치하는 의료진 정보가 없어 예외가 발생해야 한다.");
    }
}