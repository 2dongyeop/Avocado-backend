package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.hospital.Hospital;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.hospital.HospitalRepository;
import io.wisoft.capstonedesign.hospital.HospitalService;
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
public class HospitalServiceTest {

    @Autowired
    HospitalService hospitalService;
    @Autowired HospitalRepository hospitalRepository;

    @Test
    public void 병원_저장() throws Exception {
        //given -- 조건
        Hospital hospital = Hospital.createHospital("avocado", "042", "대전", "연중무휴");

        //when -- 동작
        Long saveId = hospitalService.save(hospital);

        //then -- 검증
        Hospital getHospital = hospitalService.findOne(saveId);
        Assertions.assertThat(getHospital).isEqualTo(hospital);
    }

    @Test(expected = NullHospitalException.class)
    public void 병원_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        hospitalService.findOne(100L);

        //then -- 검증
        fail("해당 hospitalId에 일치하는 병원이 없어 오류가 발생해야 한다.");
    }
}