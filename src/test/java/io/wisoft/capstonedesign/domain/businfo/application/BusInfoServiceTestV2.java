package io.wisoft.capstonedesign.domain.businfo.application;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfoRepository;
import io.wisoft.capstonedesign.domain.businfo.web.dto.CreateBusInfoRequest;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BusInfoServiceTestV2 {

    @InjectMocks
    private BusInfoService busInfoService;

    @Mock
    private BusInfoRepository busInfoRepository;


    @Nested
    @DisplayName("버스정보 등록")
    class CreateBusInfo {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 버스 정보가 저장되어야 한다.")
        void 성공() throws Exception {

            //given
            final var request = new CreateBusInfoRequest(
                    "busInfoPath",
                    "DAEJEON"
            );

            final BusInfo busInfo = BusInfo.createBusInfo(
                    "busInfoPath",
                    BusArea.DAEJEON
            );
            given(busInfoRepository.save(any())).willReturn(busInfo);

            //when
            busInfoService.save(request);

            //then
            verify(busInfoRepository, times(1)).save(any());
        }
    }


    @Nested
    @DisplayName("버스정보 삭제")
    class DeleteBusInfo {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 버스 정보가 삭제되어야 한다.")
        void 성공() throws Exception {

            //given
            final Long busInfoId = 1L;
            final BusInfo busInfo = BusInfo.createBusInfo(
                    "busInfoPath",
                    BusArea.DAEJEON
            );

            given(busInfoRepository.findById(any())).willReturn(Optional.of(busInfo));

            //when
            busInfoService.delete(busInfoId);

            //then
            verify(busInfoRepository, times(1)).delete(any());
        }
    }
}
