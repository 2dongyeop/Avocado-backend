package io.wisoft.capstonedesign.domain.hospital.web;


import feign.Param;
import feign.RequestLine;
import io.wisoft.capstonedesign.domain.hospital.web.dto.HospitalInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * API Interface
 * 호출하고자 하는 API를 선언한다.
 */
public interface OpenDataApi {

    /**
     * 병원 상세정보 조회하는 API/hospInfoServicev2/getHospBasisList
     */
    @RequestLine("GET /hospInfoServicev2/getHospBasisList" +
            "?ServiceKey={serviceKey}&pageNo={pageNumber}&numOfRows={rowsPerPage}")
    HospitalInfoResponse getHospitalInfo(
        @Param("serviceKey") String serviceKey,
        @Param("pageNumber") String pageNumber,
        @Param("rowsPerPage") String rowsPerPage);
}
