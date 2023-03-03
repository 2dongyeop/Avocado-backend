package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.exception.duplicate.DuplicateStaffException;
import io.wisoft.capstonedesign.repository.StaffRepository;
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

    @Autowired StaffService staffService;
    @Autowired StaffRepository staffRepository;

    @Test
    public void 의료진가입() throws Exception {
        //given -- 조건
        Staff staff = Staff.newInstance("lee", "ldy_1204@naver.com", "1111", "0000", "hhhh", "안과");

        //when -- 동작
        Long signUpId = staffService.signUp(staff);

        //then -- 검증
        Assertions.assertThat(staff).isEqualTo(staffRepository.findOne(signUpId));
    }

    @Test(expected = DuplicateStaffException.class)
    public void 의료진중복검증() throws Exception {
        //given -- 조건
        Staff staff1 = Staff.newInstance("lee", "ldy_1204@naver.com", "1111", "0000", "hhhh", "안과");
        Staff staff2 = Staff.newInstance("dong", "ldy_1204@naver.com", "1111", "0000", "hhhh", "안과");

        //when -- 동작
        staffService.signUp(staff1);
        staffService.signUp(staff2); //예외 발생!

        //then -- 검증
        fail("의료진의 이메일이 중복되어 예외가 발생해야 한다.");
    }
}