package io.wisoft.capstonedesign.domain.businfo.application;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.domain.businfo.web.dto.CreateBusInfoRequest;
import io.wisoft.capstonedesign.global.enumeration.status.BusInfoStatus;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBusInfoException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    public void findByArea() throws Exception {
        //given -- 조건

        //when -- 동작
        List<BusInfo> list = busInfoService.findByArea("DAEJEON");

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test(expected = IllegalValueException.class)
    @DisplayName("Enum이 아닌 값을 넣어 찾을 수 없는 경우")
    public void findByArea_fail_NotEnum() throws Exception {
        //given -- 조건
        String area = "HANBAT";

        //when -- 동작
        List<BusInfo> list = busInfoService.findByArea(area);

        //then -- 검증
        fail();
    }
}