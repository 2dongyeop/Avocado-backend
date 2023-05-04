package io.wisoft.capstonedesign.domain.hospital.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalRequest;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalResponse;
import io.wisoft.capstonedesign.domain.hospital.web.dto.HospitalDto;
import io.wisoft.capstonedesign.domain.hospital.web.dto.Result;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import io.wisoft.capstonedesign.global.config.OpenAPIConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Tag(name = "병원정보")
@RestController
@RequiredArgsConstructor
public class HospitalApiController {

    private final HospitalService hospitalService;

    /* 병원 저장 */
    @PostMapping("/api/hospitals")
    public CreateHospitalResponse createHospitalRequest(
            @RequestBody @Valid final CreateHospitalRequest request) {

        final Long id = hospitalService.save(request);
        final Hospital getHospital = hospitalService.findById(id);
        return new CreateHospitalResponse(getHospital.getId());
    }


    /* 병원 단건 조회 */
    @GetMapping("/api/hospitals/{id}/details")
    public Result hospital(@PathVariable final Long id) {
        return new Result(new HospitalDto(hospitalService.findById(id)));
    }


    @SwaggerApi(summary = "병원 목록 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/api/hospitals")
    public Result hospitals() {
        return new Result(hospitalService.findAll()
                .stream().map(HospitalDto::new)
                .collect(Collectors.toList()));
    }

    /**
     *  Open API를 이용해 공공데이터 가져오기
     */
    @GetMapping("/api/hospinfo")
    public String getHospitalInfo() throws IOException {

        /**
         * 이 포스팅은 "건강보험심사평가원_병원정보서비스" 를 이용하였습니다.
         * 아래 링크 참고
         * https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15001698
         */

        String baseURL = "https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList";

        String secretKey = "?ServiceKey=" + OpenAPIConfig.OpenAPISecretKey;
//        String pageNo = "&pageNo=1";
//        String sidoCd = "&sidoCd=250000";
        String apiURL = baseURL + secretKey;

        /**
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(apiURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");


        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        return sb.toString();
    }
}