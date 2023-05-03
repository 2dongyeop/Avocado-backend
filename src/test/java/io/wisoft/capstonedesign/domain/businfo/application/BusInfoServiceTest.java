package io.wisoft.capstonedesign.domain.businfo.application;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.domain.businfo.web.dto.CreateBusInfoRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBusInfoException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        Assertions.assertThat(busInfo.getBusInfoPath()).isEqualTo(request.busInfoPath());
        Assertions.assertThat(busInfo.getArea().toString()).isEqualTo(request.area());
    }

    @Test
    public void 버스정보_삭제() throws Exception {
        //given -- 조건

        CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");
        Long saveId = busInfoService.save(request);

        //when -- 동작
        busInfoService.delete(saveId);

        //then -- 검증
        assertThrows(NullBusInfoException.class, () -> {
            busInfoService.findById(saveId);
        });
    }

    @Test
    public void 버스정보_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        //then -- 검증
        assertThrows(NullBusInfoException.class, () -> {
            busInfoService.findById(100L);
        });
    }

    @Test
    public void 버스정보_삭제요청_중복() throws Exception {
        //given -- 조건

        CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");
        Long saveId = busInfoService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NullBusInfoException.class, () -> {
            busInfoService.delete(saveId);
            busInfoService.delete(saveId);
        });
    }

    @Test
    public void findByArea() throws Exception {
        //given -- 조건

        //when -- 동작
        List<BusInfo> list = busInfoService.findByArea("DAEJEON");

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Enum이 아닌 값을 넣어 찾을 수 없는 경우")
    public void findByArea_fail_NotEnum() throws Exception {
        //given -- 조건
        String area = "HANBAT";

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalValueException.class, () -> {
            List<BusInfo> list = busInfoService.findByArea(area);
        });
    }
}