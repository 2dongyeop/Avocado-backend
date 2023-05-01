package io.wisoft.capstonedesign.domain.hospital.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalRequest;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateHospitalException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHospitalException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class HospitalServiceTest {

    @Autowired
    HospitalService hospitalService;

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
        final CreateHospitalRequest request = new CreateHospitalRequest("avocado", "042", "대전", "연중무휴");
        final Long saveId = hospitalService.save(request);

        //when -- 동작
        final Hospital hospital = hospitalService.findById(saveId);

        //then -- 검증
        Assertions.assertThat(hospital).isNotNull();
        Assertions.assertThat(hospital.getName()).isEqualTo("avocado");
    }


    @Test
    public void 병원_단건_조회_실패() throws Exception {
        //given -- 조건
        final CreateHospitalRequest request = new CreateHospitalRequest("avocado", "042", "대전", "연중무휴");
        final Long saveId = hospitalService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullHospitalException.class, () -> {
            hospitalService.findById(100L);
        });
    }

    @Test
    public void 병원_중복_저장() throws Exception {
        //given -- 조건

        final CreateHospitalRequest request1 = new CreateHospitalRequest("avocado", "02", "서울", "연중무휴");
        final CreateHospitalRequest request2 = new CreateHospitalRequest("avocado", "042", "대전", "연중무휴");

        //when -- 동작
        //then -- 검증
        assertThrows(DuplicateHospitalException.class, () -> {
            hospitalService.save(request1);
            hospitalService.save(request2);
        });
    }
}