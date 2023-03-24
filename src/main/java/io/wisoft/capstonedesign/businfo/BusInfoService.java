package io.wisoft.capstonedesign.businfo;

import io.wisoft.capstonedesign.businfo.BusInfo;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullBusInfoException;
import io.wisoft.capstonedesign.businfo.BusInfoRepository;
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
    public Long save(final BusInfo busInfo) {

        busInfoRepository.save(busInfo);
        return busInfo.getId();
    }

    /**
     * 버스정보 삭제
     */
    @Transactional
    public void delete(final Long busInfoId) {

        BusInfo busInfo = busInfoRepository.findOne(busInfoId);
        busInfo.delete();
    }


    /* 조회 로직 */
    public BusInfo findOne(final Long busInfoId) {
        BusInfo busInfo = busInfoRepository.findOne(busInfoId);

        if (busInfo == null) {
            throw new NullBusInfoException("해당 버스정보는 존재하지 않습니다.");
        }
        return busInfo;
    }

    public List<BusInfo> findAll() {
        return busInfoRepository.findAll();
    }

    public List<BusInfo> findByArea(final String area) {
        BusArea busArea = BusArea.valueOf(area);
        List<BusInfo> busInfoList = busInfoRepository.findByArea(busArea);

        if (busInfoList.size() == 0) {
            throw new NullBusInfoException("해당 버스정보는 존재하지 않습니다.");
        }
        return busInfoList;
    }
}
