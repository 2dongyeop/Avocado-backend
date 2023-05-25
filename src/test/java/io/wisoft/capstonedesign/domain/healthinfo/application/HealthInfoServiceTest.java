package io.wisoft.capstonedesign.domain.healthinfo.application;

import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.CreateHealthInfoRequest;
import io.wisoft.capstonedesign.domain.staff.application.StaffService;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullHealthInfoException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class HealthInfoServiceTest {
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
        assertThrows(NullHealthInfoException.class, () -> {
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
        assertThrows(NullHealthInfoException.class, () -> {
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
        assertThrows(NullHealthInfoException.class, () -> {
            healthInfoService.findById(100L);
        });
    }

    @Test
    public void findAllByDept() throws Exception {
        //given -- 조건
        final String dept = "DENTAL";
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<HealthInfo> list = healthInfoService.findAllByDeptUsingPaging(dept, request).getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("해당하는 dept가 없어 오류가 발생해야한다.")
    public void findAllByDept_fail() throws Exception {
        //given -- 조건
        final String dept = "1231231";
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createAt"));


        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            healthInfoService.findAllByDeptUsingPaging(dept, request).getContent();
        });
    }


    @Test
    public void paging() throws Exception {
        //given -- 조건
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<HealthInfo> list = healthInfoService.findByUsingPaging(request).getContent();

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    private static CreateHealthInfoRequest getCreateHealthInfoRequest() {
        return new CreateHealthInfoRequest(1L, "안경 제대로 쓰기", "OPHTHALMOLOGY", "경로");
    }
}