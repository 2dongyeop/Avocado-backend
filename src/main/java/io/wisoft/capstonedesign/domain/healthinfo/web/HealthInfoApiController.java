package io.wisoft.capstonedesign.domain.healthinfo.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
import io.wisoft.capstonedesign.domain.healthinfo.application.HealthInfoService;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@Tag(name = "건강정보")
@RestController
@RequiredArgsConstructor
public class HealthInfoApiController {

    private final HealthInfoService healthInfoService;


    @Operation(summary = "건강 정보 등록")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/api/health-infos/new")
    public CreateHealthInfoResponse createHealthInfo(
            @RequestBody @Valid final CreateHealthInfoRequest request) {

        final Long id = healthInfoService.save(request);
        final HealthInfo healthInfo = healthInfoService.findById(id);
        return new CreateHealthInfoResponse(healthInfo.getId());
    }


    @Operation(summary = "건강 정보 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(schema = @Schema(implementation = Error.class))),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @DeleteMapping("/api/health-infos/{id}")
    public DeleteHealthInfoResponse deleteHealthInfo(@PathVariable("id") final Long id) {
        healthInfoService.delete(id);
        return new DeleteHealthInfoResponse(id);
    }


    @Operation(summary = "건강 정보 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/health-infos/{id}/details")
    public Result healthInfo(@PathVariable("id") final Long id) {
        return new Result(new HealthInfoDto(healthInfoService.findDetailById(id)));
    }


    /**
     * ex) /api/health-infos/department?page=0&size=5&sort=createAt,desc
     */
    @Operation(summary = "특정 병과의 건강 정보 목록 페이징 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/health-infos/department")
    public Page<HealthInfoDto> healthInfosByDepartmentUsingPaging(
            @RequestBody @Valid final HealthInfoByDepartmentRequest request, final Pageable pageable) {

        return healthInfoService.findAllByDeptUsingPaging(request.department(), pageable)
                .map(HealthInfoDto::new);
    }


    /**
     * ex) /api/health-infos?page=0&size=5&sort=createAt,desc
     */
    @Operation(summary = "건강정보 목록을 페이지별로 조회하기")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/health-infos")
    public Page<HealthInfoDto> healthInfosUsingPaging(final Pageable pageable) {
        return healthInfoService.findByUsingPaging(pageable).map(HealthInfoDto::new);
    }
}
