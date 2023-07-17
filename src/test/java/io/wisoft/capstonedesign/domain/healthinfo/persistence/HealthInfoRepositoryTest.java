package io.wisoft.capstonedesign.domain.healthinfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.HospitalDept;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class HealthInfoRepositoryTest {

    @Autowired
    HealthInfoRepository healthInfoRepository;

    @Test
    public void findByUsingPaging() throws Exception {
        //given -- 조건
        final HospitalDept hospitalDept = HospitalDept.DENTAL;
        List<HospitalDept> hospitalDeptListlist = new ArrayList<>();
        hospitalDeptListlist.add(hospitalDept);
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final List<HealthInfo> list = healthInfoRepository.findAllByDeptUsingPagingMultiValue(hospitalDeptListlist, request).getContent();

        //then -- 검증
        assertThat(list.size()).isGreaterThan(1);
    }

    @Test
    public void paging() throws Exception {
        //given -- 조건
        final PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdAt"));

        //when -- 동작
        final Page<HealthInfo> page = healthInfoRepository.findByUsingPaging(request);
        final List<HealthInfo> content = page.getContent();

        //then -- 검증
        assertThat(content.size()).isGreaterThan(1); //콘텐트 사이즈
        assertThat(page.getTotalElements()).isGreaterThan(1); //요소개수
        assertThat(page.getNumber()).isEqualTo(0);  //페이지번호
        assertThat(page.getTotalPages()).isGreaterThan(1); //총 페이지 수
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }
}