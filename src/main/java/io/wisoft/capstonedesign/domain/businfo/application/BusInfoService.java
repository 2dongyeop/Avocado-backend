package io.wisoft.capstonedesign.domain.businfo.application;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.domain.businfo.web.dto.CreateBusInfoRequest;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBusInfoException;
import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BusInfoService {

    private final BusInfoRepository busInfoRepository;

    /**
     * 버스정보 등록
     */
    @Transactional
    public Long save(final CreateBusInfoRequest request) {

        BusInfo busInfo = BusInfo.createBusInfo(request.getBusInfoPath(), BusArea.valueOf(request.getArea()));
        busInfoRepository.save(busInfo);
        return busInfo.getId();
    }

    /**
     * 버스정보 삭제
     */
    @Transactional
    public void delete(final Long busInfoId) {

        BusInfo busInfo = findById(busInfoId);
        busInfo.delete();
    }


    /* 조회 로직 */
    public BusInfo findById(final Long busInfoId) {
        return busInfoRepository.findById(busInfoId).orElseThrow(NullBusInfoException::new);
    }

    public List<BusInfo> findAll() {
        return busInfoRepository.findAll();
    }

    public List<BusInfo> findByArea(final String area) {
        List<BusInfo> busInfoList = busInfoRepository.findByArea(BusArea.valueOf(area));

        if (busInfoList.size() == 0) {
            throw new NullBusInfoException("해당 버스정보는 존재하지 않습니다.");
        }
        return busInfoList;
    }
}
