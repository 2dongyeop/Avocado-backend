package io.wisoft.capstonedesign.api;

import io.wisoft.capstonedesign.domain.Hospital;
import io.wisoft.capstonedesign.service.HospitalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HospitalApiController {

    private final HospitalService hospitalService;

    /* 병원 저장 */
    @PostMapping("/api/hospitals/new")
    public CreateHospitalResponse createHospitalRequest(
            @RequestBody @Valid final CreateHospitalRequest request) {

        Hospital hospital = Hospital.createHospital(
                request.name, request.number, request.address, request.operatingTime
        );

        Long id = hospitalService.save(hospital);
        Hospital getHospital = hospitalService.findOne(id);
        return new CreateHospitalResponse(getHospital.getId());
    }


    /* 병원 단건 조회 */
    @GetMapping("/api/hospitals/{id}")
    public Result hospital(@PathVariable final Long id) {
        Hospital hospital = hospitalService.findOne(id);

        return new Result(new HospitalDto(hospital));
    }


    /* 병원 목록 조회 */
    @GetMapping("/api/hospitals")
    public Result hospitals() {
        List<HospitalDto> hospitalDtoList = hospitalService.findAll()
                .stream().map(HospitalDto::new)
                .collect(Collectors.toList());

        return new Result(hospitalDtoList);
    }


    @Data
    @AllArgsConstructor
    static class HospitalDto {
        private String name;
        private String address;
        private String number;
        private String operatingTime;

        public HospitalDto(final Hospital hospital) {
            name = hospital.getName();
            address = hospital.getAddress();
            number = hospital.getNumber();
            operatingTime = hospital.getOperatingTime();
        }
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class CreateHospitalResponse {
        private Long id;
    }


    @Data
    static class CreateHospitalRequest {
        private String name;
        private String number;
        private String address;
        private String operatingTime;
    }
}