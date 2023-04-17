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

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HospitalApiController {

    private final HospitalService hospitalService;

    /* 병원 저장 */
    @PostMapping("/api/hospitals/new")
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


    /* 병원 목록 조회 */
    @GetMapping("/api/hospitals")
    public Result hospitals() {
        return new Result(hospitalService.findAll()
                .stream().map(HospitalDto::new)
                .collect(Collectors.toList()));
    }
}