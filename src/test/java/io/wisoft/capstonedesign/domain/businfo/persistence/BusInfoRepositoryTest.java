package io.wisoft.capstonedesign.domain.businfo.persistence;

import io.wisoft.capstonedesign.global.enumeration.BusArea;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BusInfoRepositoryTest {

    @Autowired BusInfoRepository busInfoRepository;

    @Test
    public void findByArea() throws Exception {
        //given -- 조건
        final BusArea busArea = BusArea.DAEJEON;

        //when -- 동작
        final List<BusInfo> list = busInfoRepository.findByArea(busArea);

        //then -- 검증
        assertThat(list.size()).isEqualTo(1);
    }


    @Test
    public void findByArea_fail() throws Exception {
        //given -- 조건
        final String area = "DAAAJEON";

        //when -- 동작
        assertThrows(IllegalArgumentException.class, () -> {
            busInfoRepository.findByArea(BusArea.valueOf(area));
        });

        //then -- 검증
    }
}