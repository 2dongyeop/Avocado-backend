package io.wisoft.capstonedesign.domain.hospital.web;

import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalRequest;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalResponse;
import io.wisoft.capstonedesign.domain.hospital.web.dto.HospitalDto;
import io.wisoft.capstonedesign.domain.hospital.web.dto.Result;
import jakarta.validation.Valid;
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

        Long id = hospitalService.save(request);
        Hospital getHospital = hospitalService.findById(id);
        return new CreateHospitalResponse(getHospital.getId());
    }


    /* 병원 단건 조회 */
    @GetMapping("/api/hospitals/{id}/details")
    public Result hospital(@PathVariable final Long id) {
        Hospital hospital = hospitalService.findById(id);

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
}