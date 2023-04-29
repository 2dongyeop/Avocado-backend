package io.wisoft.capstonedesign.domain.hospital.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
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

@Tag(name = "병원정보")
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


    @Operation(summary = "병원 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/hospitals")
    public Result hospitals() {
        return new Result(hospitalService.findAll()
                .stream().map(HospitalDto::new)
                .collect(Collectors.toList()));
    }
}