package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.global.enumeration.status.HealthInfoStatus;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHealthInfoException;
import io.wisoft.capstonedesign.domain.healthinfo.application.HealthInfoService;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HealthInfoServiceTest {
    @Autowired StaffService staffService;
    @Autowired HealthInfoService healthInfoService;

    @Test
    public void 건강정보_저장() throws Exception {
        //given -- 조건
        CreateHealthInfoRequest request = new CreateHealthInfoRequest(1L, "안경 제대로 쓰기", "OPHTHALMOLOGY", "경로");

        //when -- 동작
        Long saveId = healthInfoService.save(request);

        //then -- 검증
        HealthInfo healthInfo = healthInfoService.findById(saveId);

        Assertions.assertThat(healthInfo.getStatus()).isEqualTo(HealthInfoStatus.WRITE);
        Assertions.assertThat(healthInfo.getTitle()).isEqualTo(request.getTitle());
        Assertions.assertThat(healthInfo.getDept().toString()).isEqualTo(request.getDept());
    }

    @Test
    public void 건강정보_삭제() throws Exception {
        //given -- 조건
        CreateHealthInfoRequest request = new CreateHealthInfoRequest(1L, "안경 제대로 쓰기", "OPHTHALMOLOGY", "경로");

        Long saveId = healthInfoService.save(request);

        //when -- 동작
        HealthInfo getHealthInfo = healthInfoService.findById(saveId);
        getHealthInfo.delete();

        //then -- 검증
        Assertions.assertThat(getHealthInfo.getStatus()).isEqualTo(HealthInfoStatus.DELETE);
    }

    @Test(expected = IllegalStateException.class)
    public void 건강정보_삭제요청_중복() throws Exception {
        //given -- 조건
        CreateHealthInfoRequest request = new CreateHealthInfoRequest(1L, "안경 제대로 쓰기", "OPHTHALMOLOGY", "경로");
        Long saveId = healthInfoService.save(request);

        //when -- 동작
        HealthInfo getHealthInfo = healthInfoService.findById(saveId);
        getHealthInfo.delete();
        getHealthInfo.delete();

        //then -- 검증
        fail("중복 삭제 요청으로 인해 예외가 발생해야 한다.");
    }

    @Test(expected = NullHealthInfoException.class)
    public void 건강정보_단건조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        healthInfoService.findById(100L);

        //then -- 검증
        fail("단건 조회 실패로 예외가 발생해야 한다.");
    }

    @Test
    public void findAllByDept() throws Exception {
        //given -- 조건
        String dept = "DENTAL";

        //when -- 동작
        List<HealthInfo> list = healthInfoService.findAllByDept(dept);

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }


    @Test(expected = IllegalValueException.class)
    @DisplayName("해당하는 dept가 없어 오류가 발생해야한다.")
    public void findAllByDept_fail() throws Exception {
        //given -- 조건
        String dept = "1231231";

        //when -- 동작
        List<HealthInfo> list = healthInfoService.findAllByDept(dept);

        //then -- 검증
        fail();
    }


    @Test
    public void paging() throws Exception {
        //given -- 조건

        //when -- 동작
        List<HealthInfo> list = healthInfoService.findByUsingPagingOrderByCreateAtAsc(0);

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}