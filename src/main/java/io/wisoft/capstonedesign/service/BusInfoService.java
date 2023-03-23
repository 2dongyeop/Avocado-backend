package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.BusInfo;
import io.wisoft.capstonedesign.domain.enumeration.BusArea;
import io.wisoft.capstonedesign.exception.nullcheck.NullBusInfoException;
import io.wisoft.capstonedesign.repository.BusInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
//        busInfoRepository.delete(busInfoId);
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
