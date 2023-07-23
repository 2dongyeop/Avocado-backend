package io.wisoft.capstonedesign.domain.businfo.application;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.domain.businfo.web.dto.CreateBusInfoRequest;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import io.wisoft.capstonedesign.setting.common.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusInfoServiceTest extends ServiceTest {

    @Autowired
    private BusInfoService busInfoService;

    @Test
    void 버스정보_등록() throws Exception {
        //given -- 조건
        final CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");

        //when -- 동작
        final Long saveId = busInfoService.save(request);

        //then -- 검증
        final BusInfo busInfo = busInfoService.findById(saveId);

        Assertions.assertThat(busInfo.getBusInfoPath()).isEqualTo(request.busInfoPath());
        Assertions.assertThat(busInfo.getArea().toString()).isEqualTo(request.area());
    }

    @Test
    void 버스정보_삭제() throws Exception {
        //given -- 조건

        final CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");
        final Long saveId = busInfoService.save(request);

        //when -- 동작
        busInfoService.delete(saveId);

        //then -- 검증
        assertThrows(NotFoundException.class, () -> {
            busInfoService.findById(saveId);
        });
    }

    @Test
    void 버스정보_단건_조회_실패() throws Exception {
        //given -- 조건

        //when -- 동작
        //then -- 검증
        assertThrows(NotFoundException.class, () -> {
            busInfoService.findById(100L);
        });
    }

    @Test
    void 버스정보_삭제요청_중복() throws Exception {
        //given -- 조건

        final CreateBusInfoRequest request = new CreateBusInfoRequest("버스정보경로", "DAEJEON");
        Long saveId = busInfoService.save(request);

        //when -- 동작
        //then -- 검증
        assertThrows(NotFoundException.class, () -> {
            busInfoService.delete(saveId);
            busInfoService.delete(saveId);
        });
    }

    @Test
    void findByArea() throws Exception {
        //given -- 조건

        //when -- 동작
        final List<BusInfo> list = busInfoService.findByArea("DAEJEON");

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Enum이 아닌 값을 넣어 찾을 수 없는 경우")
    void findByArea_fail_NotEnum() throws Exception {
        //given -- 조건
        final String area = "HANBAT";

        //when -- 동작
        //then -- 검증
        assertThrows(IllegalArgumentException.class, () -> {
            busInfoService.findByArea(area);
        });
    }
}