package io.wisoft.capstonedesign.domain.hospital.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class HospitalRepositoryTest {

    @Autowired HospitalRepository hospitalRepository;

    @Test
    public void findByHospitalName() throws Exception {
        //given -- 조건
        String hospitalName = "서울대병원";

        //when -- 동작
        List<Hospital> list = hospitalRepository.findByName(hospitalName);

        //then -- 검증
        assertThat(list.size()).isEqualTo(1);
    }
}