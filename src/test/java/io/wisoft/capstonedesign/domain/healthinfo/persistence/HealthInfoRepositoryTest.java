package io.wisoft.capstonedesign.domain.healthinfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class HealthInfoRepositoryTest {

    @Autowired HealthInfoRepository healthInfoRepository;

    @Test
    public void findByAllDept() throws Exception {
        //given -- 조건
        HospitalDept hospitalDept = HospitalDept.DENTAL;
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createAt"));

        //when -- 동작
        List<HealthInfo> list = healthInfoRepository.findAllByDeptUsingPaging(hospitalDept, request).getContent();

        //then -- 검증
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void paging() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createAt"));

        //when -- 동작
        Page<HealthInfo> page = healthInfoRepository.findByUsingPaging(request);
        List<HealthInfo> content = page.getContent();

        //then -- 검증
        assertThat(content.size()).isEqualTo(2); //콘텐트 사이즈
        assertThat(page.getTotalElements()).isEqualTo(2); //요소개수
        assertThat(page.getNumber()).isEqualTo(0);  //페이지번호
        assertThat(page.getTotalPages()).isEqualTo(1); //총 페이지 수
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
    }
}