package io.wisoft.capstonedesign.domain.hospital.application;

import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalRequest;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateHospitalException;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class HospitalServiceTest extends ServiceTest {

    @Autowired
    HospitalService hospitalService;

    @Test
    public void 병원_저장() throws Exception {
        //given -- 조건
        final CreateHospitalRequest request = getCreateHospitalRequest("042", "대전");

        //when -- 동작
        final Long saveId = hospitalService.save(request);

        //then -- 검증
        final Hospital hospital = hospitalService.findById(saveId);
        Assertions.assertThat(hospital.getName()).isEqualTo(request.name());
    }



    @Test
    public void 병원_단건_조회() throws Exception {
        //given -- 조건
        final CreateHospitalRequest request = getCreateHospitalRequest("042", "대전");
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
        final CreateHospitalRequest request = getCreateHospitalRequest("042", "대전");
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

        final CreateHospitalRequest request1 = getCreateHospitalRequest("02", "서울");
        final CreateHospitalRequest request2 = getCreateHospitalRequest("042", "대전");

        //when -- 동작
        //then -- 검증
        assertThrows(DuplicateHospitalException.class, () -> {
            hospitalService.save(request1);
            hospitalService.save(request2);
        });
    }
    private CreateHospitalRequest getCreateHospitalRequest(final String number, final String area) {
        return new CreateHospitalRequest("avocado", number, area, "연중무휴");
    }
}