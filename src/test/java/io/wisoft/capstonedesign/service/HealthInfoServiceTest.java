package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.HealthInfo;
import io.wisoft.capstonedesign.domain.Staff;
import io.wisoft.capstonedesign.domain.enumeration.HealthInfoStatus;
import io.wisoft.capstonedesign.domain.enumeration.HospitalDept;
import io.wisoft.capstonedesign.exception.nullcheck.NullHealthInfoException;
import io.wisoft.capstonedesign.repository.HealthInfoRepository;
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
public class HealthInfoServiceTest {
    @Autowired StaffService staffService;
    @Autowired HealthInfoService healthInfoService;
    @Autowired HealthInfoRepository healthInfoRepository;

    @Test
    public void 건강정보_저장() throws Exception {
        //given -- 조건
        Staff staff = staffService.findOne(1L);

        HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, "칼럼 경로", "안경 제대로 쓰기", HospitalDept.OPHTHALMOLOGY);

        //when -- 동작
        Long saveId = healthInfoService.save(healthInfo);

        //then -- 검증
        HealthInfo getHealthInfo = healthInfoService.findOne(saveId);

        Assertions.assertThat(getHealthInfo).isEqualTo(healthInfo);
        Assertions.assertThat(getHealthInfo.getStatus()).isEqualTo(HealthInfoStatus.WRITE);
        Assertions.assertThat(getHealthInfo.getTitle()).isEqualTo("안경 제대로 쓰기");
        Assertions.assertThat(getHealthInfo.getDept()).isEqualTo(HospitalDept.OPHTHALMOLOGY);
    }

    @Test
    public void 건강정보_삭제() throws Exception {
        //given -- 조건
        Staff staff = staffService.findOne(1L);

        HealthInfo healthInfo = HealthInfo.createHealthInfo(staff ,"칼럼 경로", "안경 제대로 쓰기", HospitalDept.OPHTHALMOLOGY);

        Long saveId = healthInfoService.save(healthInfo);

        //when -- 동작
        HealthInfo getHealthInfo = healthInfoService.findOne(saveId);
        getHealthInfo.delete();

        //then -- 검증
        Assertions.assertThat(getHealthInfo.getStatus()).isEqualTo(HealthInfoStatus.DELETE);
    }

    @Test(expected = IllegalStateException.class)
    public void 건강정보_삭제요청_중복() throws Exception {
        //given -- 조건
        Staff staff = staffService.findOne(1L);

        HealthInfo healthInfo = HealthInfo.createHealthInfo(staff, "칼럼 경로", "안경 제대로 쓰기", HospitalDept.OPHTHALMOLOGY);

        Long saveId = healthInfoService.save(healthInfo);

        //when -- 동작
        HealthInfo getHealthInfo = healthInfoService.findOne(saveId);
        getHealthInfo.delete();
        getHealthInfo.delete();

        //then -- 검증
        fail("중복 삭제 요청으로 인해 예외가 발생해야 한다.");
    }

    //건강정보 단건 조회 실패
    @Test(expected = NullHealthInfoException.class)
    public void 건강정보_단건조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        healthInfoService.findOne(100L);

        //then -- 검증
        fail("단건 조회 실패로 예외가 발생해야 한다.");
    }
}