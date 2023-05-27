package io.wisoft.capstonedesign.domain.businfo.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.businfo.application.BusInfoService;
import io.wisoft.capstonedesign.domain.businfo.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

@Tag(name = "셔틀버스")
@RestController
@RequiredArgsConstructor
public class BusInfoApiController {

    private final BusInfoService busInfoService;

    @SwaggerApi(summary = "셔틀버스 정보 등록", implementation = CreateBusInfoResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping("/api/bus-info")
    public CreateBusInfoResponse createBusInfo(
            @RequestBody @Valid final CreateBusInfoRequest request) {
        return new CreateBusInfoResponse(busInfoService.save(request));
    }


    @SwaggerApi(summary = "셔틀 버스 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/api/bus-info/{id}/details")
    public Result busInfo(@PathVariable final Long id) {
        return new Result(new BusInfoDto(busInfoService.findById(id)));
    }


    @SwaggerApi(summary = "특정 지역 셔틀 버스 정보 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/api/bus-info/area/details")
    public Result busInfoByArea(
            @RequestBody @Valid final BusInfoByAreaRequest request) {

        validateArea(request.area());

        return new Result(busInfoService.findByArea(request.area())
                .stream().map(BusInfoDto::new)
                .collect(Collectors.toList()));
    }

    private boolean validateArea(final String area) {

        final Iterator<BusArea> iterator = Arrays.stream(BusArea.values()).iterator();

        while (iterator.hasNext()) {
            BusArea busArea = iterator.next();

            if (busArea.getCode().equals(area.toUpperCase())) {
                return true;
            }
        }
        throw new IllegalValueException("일치하는 BusArea가 없습니다.");
    }

    @SwaggerApi(summary = "셔틀 버스 정보 삭제", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/api/bus-info/{id}")
    public Result delete(@PathVariable final Long id) {
        busInfoService.delete(id);
        return new Result(new DeleteBusInfoResponse(id));
    }
}