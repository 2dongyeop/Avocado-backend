package io.wisoft.capstonedesign.domain.businfo.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.appointment.web.dto.CreateAppointmentResponse;
import io.wisoft.capstonedesign.domain.businfo.application.BusInfoService;
import io.wisoft.capstonedesign.domain.businfo.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "셔틀버스")
@RestController
@RequiredArgsConstructor
public class BusInfoApiController {

    private final BusInfoService busInfoService;

    @Operation(summary = "셔틀버스 정보 등록")
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
    @PostMapping("/api/bus-info/new")
    public CreateBusInfoResponse createBusInfo(
            @RequestBody @Valid final CreateBusInfoRequest request) {
        return new CreateBusInfoResponse(busInfoService.save(request));
    }


    @Operation(summary = "셔틀 버스 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/bus-info/{id}/details")
    public Result busInfo(@PathVariable final Long id) {
        return new Result(new BusInfoDto(busInfoService.findById(id)));
    }


    @Operation(summary = "특정 지역 셔틀 버스 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateAppointmentResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/api/bus-info/area/details")
    public Result busInfoByArea(
            @RequestBody @Valid final BusInfoByAreaRequest request) {

        return new Result(busInfoService.findByArea(request.area())
                .stream().map(BusInfoDto::new)
                .collect(Collectors.toList()));
    }


    @Operation(summary = "셔틃 버스 정보 삭제")
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
    @DeleteMapping("/api/bus-info/{id}")
    public Result delete(@PathVariable final Long id) {
        busInfoService.delete(id);
        return new Result(new DeleteBusInfoResponse(id));
    }
}