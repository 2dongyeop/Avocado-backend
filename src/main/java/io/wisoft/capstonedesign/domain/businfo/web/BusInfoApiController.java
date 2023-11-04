package io.wisoft.capstonedesign.domain.businfo.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.businfo.application.BusInfoService;
import io.wisoft.capstonedesign.domain.businfo.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithoutAuth;
import io.wisoft.capstonedesign.global.enumeration.BusArea;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Iterator;

@Tag(name = "셔틀버스")
@Slf4j
@RestController
@RequestMapping("/api/bus-info")
@RequiredArgsConstructor
public class BusInfoApiController {

    private final BusInfoService busInfoService;

    @SwaggerApi(summary = "셔틀버스 정보 등록", implementation = CreateBusInfoResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping
    public CreateBusInfoResponse createBusInfo(
            @RequestBody @Valid final CreateBusInfoRequest request) {

        log.info("CreateBusInfoRequest[{}]", request);
        return new CreateBusInfoResponse(busInfoService.save(request));
    }


    @SwaggerApi(summary = "셔틀 버스 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/{id}/details")
    public Result busInfo(@PathVariable final Long id) {

        log.info("BusInfo Id[{}]", id);
        return new Result(new BusInfoDto(busInfoService.findById(id)));
    }


    @SwaggerApi(summary = "특정 지역 셔틀 버스 정보 조회", implementation = Result.class)
    @SwaggerApiFailWithoutAuth
    @GetMapping("/area/details")
    public Result busInfoByArea(
            @RequestBody @Valid final BusInfoByAreaRequest request) {

        log.info("BusInfoByAreaRequest[{}]", request);
        validateArea(request.area());

        return new Result(busInfoService.findByArea(request.area())
                .stream().map(BusInfoDto::new)
                .toList());
    }

    private void validateArea(final String area) {

        final Iterator<BusArea> iterator = Arrays.stream(BusArea.values()).iterator();

        while (iterator.hasNext()) {
            final BusArea busArea = iterator.next();

            if (busArea.getCode().equals(area.toUpperCase())) {
                return;
            }
        }
        log.info("area[{}] is not BusArea EnumType", area);
        throw new IllegalValueException("일치하는 BusArea가 없습니다.", ErrorCode.ILLEGAL_AREA);
    }

    @SwaggerApi(summary = "셔틀 버스 정보 삭제", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Long id) {

        log.info("BusInfo Id[{}]", id);

        busInfoService.delete(id);
        return new Result(new DeleteBusInfoResponse(id));
    }
}