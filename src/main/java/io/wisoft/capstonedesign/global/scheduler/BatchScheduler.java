package io.wisoft.capstonedesign.global.scheduler;

import feign.Feign;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.persistence.HospitalRepository;
import io.wisoft.capstonedesign.domain.hospital.web.OpenDataApi;
import io.wisoft.capstonedesign.domain.hospital.web.dto.HospitalInfoResponse;
import io.wisoft.capstonedesign.global.config.OpenAPIConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class BatchScheduler {

    private String apiKey = OpenAPIConfig.OpenAPISecretKey;

    @Autowired
    private HospitalRepository hospitalRepository;
    private OpenDataApi openDataApi;

    @PostConstruct
    public void postConstruct() {

        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding("UTF-8")
                .build();

        openDataApi = Feign.builder()
                .encoder(new JAXBEncoder(jaxbFactory))
                .decoder(new JAXBDecoder(jaxbFactory))
                .target(OpenDataApi.class, "https://apis.data.go.kr/B551182");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("BatchScheduler Finished At : {}", LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 1000)
    public void scheduleTask() {

        // 1 ~ 10 page를 가져오기
        for (int i = 1; i <= 10; i++) {
            String rowsPerPage = "10";
            String pageNumber = String.valueOf(i);

            HospitalInfoResponse hospitalInfos = openDataApi.getHospitalInfo(apiKey, pageNumber, rowsPerPage);

            log.info("====================================");
            log.info("병원 상세 정보 Page {} : {}", pageNumber, hospitalInfos.toString());

            //DB 저장
            hospitalInfos.getBody().getItems().forEach(item -> {

                log.info("Current Item : {}", item.toString());
                log.info("Save : {}",
                        hospitalRepository.save(Hospital.builder()
                                .name(item.getYadmNm())
                                .number(item.getTelno())
                                .address(item.getAddr())
                                .operatingTime("test")
                                .build()));
            });
        }
    }
}
