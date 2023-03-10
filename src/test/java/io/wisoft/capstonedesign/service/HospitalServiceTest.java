package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.repository.HospitalRepository;
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
public class HospitalServiceTest {

    @Autowired HospitalService hospitalService;
    @Autowired HospitalRepository hospitalRepository;


    @Test(expected = NullHospitalException.class)
    public void 병원_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        hospitalService.findOne(2L);

        //then -- 검증
        fail("해당 hospitalId에 일치하는 병원이 없어 오류가 발생해야 한다.");
    }
}