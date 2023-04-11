package io.wisoft.capstonedesign.domain.healthinfo.application;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHealthInfoException;
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

        Assertions.assertThat(healthInfo.getTitle()).isEqualTo(request.getTitle());
        Assertions.assertThat(healthInfo.getDept().toString()).isEqualTo(request.getDept());
    }

    @Test(expected = NullHealthInfoException.class)
    public void 건강정보_삭제() throws Exception {
        //given -- 조건
        CreateHealthInfoRequest request = new CreateHealthInfoRequest(1L, "안경 제대로 쓰기", "OPHTHALMOLOGY", "경로");

        Long saveId = healthInfoService.save(request);

        //when -- 동작
        healthInfoService.delete(saveId);

        //then -- 검증
        healthInfoService.findById(saveId);
        fail("해당 아이디가 존재하지 않아 예외가 발생");
    }

    @Test(expected = NullHealthInfoException.class)
    public void 건강정보_삭제요청_중복() throws Exception {
        //given -- 조건
        CreateHealthInfoRequest request = new CreateHealthInfoRequest(1L, "안경 제대로 쓰기", "OPHTHALMOLOGY", "경로");
        Long saveId = healthInfoService.save(request);

        //when -- 동작
        healthInfoService.delete(saveId);
        healthInfoService.delete(saveId);

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
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        List<HealthInfo> list = healthInfoService.findAllByDeptUsingPaging(dept, request).getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }


    @Test(expected = IllegalValueException.class)
    @DisplayName("해당하는 dept가 없어 오류가 발생해야한다.")
    public void findAllByDept_fail() throws Exception {
        //given -- 조건
        String dept = "1231231";
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createAt"));


        //when -- 동작
        healthInfoService.findAllByDeptUsingPaging(dept, request).getContent();

        //then -- 검증
        fail();
    }


    @Test
    public void paging() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        List<HealthInfo> list = healthInfoService.findByUsingPaging(request).getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}