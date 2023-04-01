package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.domain.businfo.application.BusInfoService;
import io.wisoft.capstonedesign.domain.businfo.web.dto.CreateBusInfoRequest;
import io.wisoft.capstonedesign.global.enumeration.status.BusInfoStatus;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBusInfoException;
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
public class BusInfoServiceTest {

    @Autowired
    BusInfoService busInfoService;

    @Test
    public void 버스정보_등록() throws Exception {
        //given -- 조건
        CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");

        //when -- 동작
        Long saveId = busInfoService.save(request);

        //then -- 검증
        BusInfo busInfo = busInfoService.findById(saveId);

        Assertions.assertThat(busInfo.getBusInfoPath()).isEqualTo(request.getBusInfoPath());
        Assertions.assertThat(busInfo.getArea().toString()).isEqualTo(request.getArea());
        Assertions.assertThat(busInfo.getStatus()).isEqualTo(BusInfoStatus.WRITE);
    }

    @Test
    public void 버스정보_삭제() throws Exception {
        //given -- 조건

        CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");
        Long saveId = busInfoService.save(request);

        BusInfo busInfo = busInfoService.findById(saveId);

        //when -- 동작
        busInfo.delete();

        //then -- 검증
        Assertions.assertThat(busInfo.getBusInfoPath()).isEqualTo(request.getBusInfoPath());
        Assertions.assertThat(busInfo.getStatus()).isEqualTo(BusInfoStatus.DELETE);
    }

    @Test(expected = NullBusInfoException.class)
    public void 버스정보_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        busInfoService.findById(100L);

        //then -- 검증
        fail("단건 조회 실패로 인해 예외가 발생해야 한다.");
    }

    @Test(expected = IllegalStateException.class)
    public void 버스정보_삭제요청_중복() throws Exception {
        //given -- 조건

        CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");
        Long saveId = busInfoService.save(request);

        BusInfo busInfo = busInfoService.findById(saveId);

        //when -- 동작
        busInfo.delete();
        busInfo.delete();

        //then -- 검증
        fail("중복 삭제 요청으로 인해 예외가 발생해야 한다.");
    }
}