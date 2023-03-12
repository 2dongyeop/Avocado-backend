package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.BusInfo;
import io.wisoft.capstonedesign.domain.enumeration.BusArea;
import io.wisoft.capstonedesign.exception.nullcheck.NullBusInfoException;
import io.wisoft.capstonedesign.repository.BusInfoRepository;
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
    public Long save(String busInfoPath, BusArea area) {

        BusInfo busInfo = BusInfo.createBusInfo(busInfoPath, area);

        busInfoRepository.save(busInfo);
        return busInfo.getId();
    }

    /**
     * 버스정보 삭제
     */
    @Transactional
    public void delete(Long busInfoId) {

        BusInfo busInfo = busInfoRepository.findOne(busInfoId);
        busInfo.delete();
    }


    /* 조회 로직 */
    public BusInfo findOne(Long busInfoId) {
        BusInfo busInfo = busInfoRepository.findOne(busInfoId);

        if (busInfo == null) {
            throw new NullBusInfoException("해당 버스정보는 존재하지 않습니다.");
        }
        return busInfo;
    }

    public List<BusInfo> findAll() {
        return busInfoRepository.findAll();
    }
}
