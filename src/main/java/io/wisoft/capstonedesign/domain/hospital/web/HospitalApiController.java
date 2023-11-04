package io.wisoft.capstonedesign.domain.hospital.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.hospital.persistence.Hospital;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalRequest;
import io.wisoft.capstonedesign.domain.hospital.web.dto.CreateHospitalResponse;
import io.wisoft.capstonedesign.domain.hospital.web.dto.HospitalDto;
import io.wisoft.capstonedesign.domain.hospital.web.dto.Result;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "병원정보")
@Slf4j
@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalApiController {

    private final HospitalService hospitalService;

    /* 병원 저장 */
    @PostMapping
    public CreateHospitalResponse createHospitalRequest(
            @RequestBody @Valid final CreateHospitalRequest request) {

        log.info("CreateHospitalRequest[{}]", request);

        final Long id = hospitalService.save(request);
        final Hospital getHospital = hospitalService.findById(id);
        return new CreateHospitalResponse(getHospital.getId());
    }


    /* 병원 단건 조회 */
    @GetMapping("/{id}/details")
    public Result hospital(@PathVariable final Long id) {

        log.info("Hospital Id[{}]", id);
        return new Result(new HospitalDto(hospitalService.findById(id)));
    }


    @SwaggerApi(summary = "병원 목록 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping
    public Result hospitals() {
        return new Result(hospitalService.findAll()
                .stream().map(HospitalDto::new)
                .toList());
    }
}