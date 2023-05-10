package io.wisoft.capstonedesign.domain.hospital.web;

import io.wisoft.capstonedesign.global.config.OpenAPIConfig;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ReadHospitalInfoController {

    //JSON 형태의 API 받아오기
    @GetMapping("/api/test")
    public Map<String, Object> readHospitalInfo() {
        Map<String, Object> map = new HashMap<>();

        String baseURL = "https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList";

        String secretKey = "?ServiceKey=" + OpenAPIConfig.OpenAPISecretKey;
//        String pageNo = "&pageNo=1";
//        String sidoCd = "&sidoCd=250000";
        String apiURL = baseURL + secretKey;

        try {

            RestTemplate restTemplate = new RestTemplate();

            //1. API를 호출하여 결과를 가져오기
            String response = restTemplate.getForObject(apiURL, String.class);

            //2. 가공된 데이터를 반환
            System.out.println(response);

            //3. DB에 저장
            JSONObject jsonObject = new JSONObject(response.toString());
//            JSONObject jsonObject = new JSONObject(response);

            System.out.println(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }
}
