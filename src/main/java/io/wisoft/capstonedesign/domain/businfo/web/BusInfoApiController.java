package io.wisoft.capstonedesign.domain.businfo.web;

import io.wisoft.capstonedesign.domain.businfo.application.BusInfoService;
import io.wisoft.capstonedesign.domain.businfo.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BusInfoApiController {

    private final BusInfoService busInfoService;

    /* 셔틀버스 정보 등록 */
    @PostMapping("/api/bus-info/new")
    public CreateBusInfoResponse createBusInfo(
            @RequestBody @Valid final CreateBusInfoRequest request) {
        return new CreateBusInfoResponse(busInfoService.save(request));
    }


    /* 셔틀 버스 단건 조회 */
    @GetMapping("/api/bus-info/{id}/details")
    public Result busInfo(@PathVariable final Long id) {
        return new Result(new BusInfoDto(busInfoService.findById(id)));
    }


    /* 특정 지역 셔틀 버스 정보 조회 */
    @GetMapping("/api/bus-info/area/details")
    public Result busInfoByArea(
            @RequestBody @Valid final BusInfoByAreaRequest request) {

        return new Result(busInfoService.findByArea(request.area())
                .stream().map(BusInfoDto::new)
                .collect(Collectors.toList()));
    }


    /* 셔틃 버스 정보 삭제 */
    @DeleteMapping("/api/bus-info/{id}")
    public Result delete(@PathVariable final Long id) {
        busInfoService.delete(id);
        return new Result(new DeleteBusInfoResponse(id));
    }
}