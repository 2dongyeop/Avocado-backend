package io.wisoft.capstonedesign.domain.healthinfo.application;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HealthInfoServiceTest extends ServiceTest {
    @Autowired StaffService staffService;
    @Autowired HealthInfoService healthInfoService;

    @Test
    public void 건강정보_저장() throws Exception {
        //given -- 조건
        final CreateHealthInfoRequest request = getCreateHealthInfoRequest();

        //when -- 동작
        final Long saveId = healthInfoService.save(request);

        //then -- 검증
        final HealthInfo healthInfo = healthInfoService.findById(saveId);

        Assertions.assertThat(healthInfo.getTitle()).isEqualTo(request.title());
        Assertions.assertThat(healthInfo.getDept().toString()).isEqualTo(request.dept());
    }

    @Test
    public void 건강정보_삭제() throws Exception {
        //given -- 조건
        final CreateHealthInfoRequest request = getCreateHealthInfoRequest();

        final Long saveId = healthInfoService.save(request);

        //when -- 동작
        healthInfoService.delete(saveId);

        //then -- 검증
        assertThrows(NotFoundException.class, () -> {
            healthInfoService.findById(saveId);
        });
    }



    @Test
    public void 건강정보_삭제요청_중복() throws Exception {
        //given -- 조건
        final CreateHealthInfoRequest request = getCreateHealthInfoRequest();
        final Long saveId = healthInfoService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NotFoundException.class, () -> {
            healthInfoService.delete(saveId);
            healthInfoService.delete(saveId);
        });
    }

    @Test
    public void 건강정보_단건조회_실패() throws Exception {
        //given -- 조건
        final CreateHealthInfoRequest request = getCreateHealthInfoRequest();
        final Long saveId = healthInfoService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NotFoundException.class, () -> {
            healthInfoService.findById(100L);
        });
    }

    @Test
    public void findAllByDept() throws Exception {
        //given -- 조건
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<HealthInfo> list = healthInfoService.findByUsingPaging(request).getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isGreaterThan(1);
    }


    @Test
    public void paging() throws Exception {
        //given -- 조건
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<HealthInfo> list = healthInfoService.findByUsingPaging(request).getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isGreaterThan(1);
    }

    private CreateHealthInfoRequest getCreateHealthInfoRequest() {
        return new CreateHealthInfoRequest(1L, "안경 제대로 쓰기", "OPHTHALMOLOGY", "경로");
    }
}