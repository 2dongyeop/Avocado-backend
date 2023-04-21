package io.wisoft.capstonedesign.domain.hospital.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateHospitalException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalRequest;
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

    @Autowired HospitalService hospitalService;

    @Test
    public void 병원_저장() throws Exception {
        //given -- 조건
        final CreateHospitalRequest request = new CreateHospitalRequest("avocado", "042", "대전", "연중무휴");

        //when -- 동작
        final Long saveId = hospitalService.save(request);

        //then -- 검증
        final Hospital hospital = hospitalService.findById(saveId);
        Assertions.assertThat(hospital.getName()).isEqualTo(request.name());
    }


    @Test
    public void 병원_단건_조회() throws Exception {
        //given -- 조건

        //when -- 동작
        Hospital hospital = hospitalService.findById(1L);

        //then -- 검증
        Assertions.assertThat(hospital).isNotNull();
        Assertions.assertThat(hospital.getName()).isEqualTo("을지대학병원");
    }


    @Test(expected = NullHospitalException.class)
    public void 병원_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        hospitalService.findById(100L);

        //then -- 검증
        fail("해당 hospitalId에 일치하는 병원이 없어 오류가 발생해야 한다.");
    }

    @Test(expected = DuplicateHospitalException.class)
    public void 병원_중복_저장() throws Exception {
        //given -- 조건

        final CreateHospitalRequest request1 = new CreateHospitalRequest("avocado", "02", "서울", "연중무휴");
        final CreateHospitalRequest request2 = new CreateHospitalRequest("avocado", "042", "대전", "연중무휴");

        //when -- 동작
        hospitalService.save(request1);
        hospitalService.save(request2);

        //then -- 검증
        fail("병원 이름이 중복되어 예외가 발생해야 한다.");
    }
}