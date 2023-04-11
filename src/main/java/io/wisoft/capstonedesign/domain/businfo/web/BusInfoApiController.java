package io.wisoft.capstonedesign.domain.businfo.web;

import io.wisoft.capstonedesign.domain.businfo.persistence.BusInfo;
import io.wisoft.capstonedesign.domain.businfo.application.BusInfoService;
import io.wisoft.capstonedesign.domain.businfo.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BusInfoApiController {

    private final BusInfoService busInfoService;

    /* 셔틀버스 정보 등록 */
    @PostMapping("/api/bus-info/new")
    public CreateBusInfoResponse createBusInfo(
            @RequestBody @Valid final CreateBusInfoRequest request) {

        Long id = busInfoService.save(request);
        return new CreateBusInfoResponse(id);
    }


    /* 셔틀 버스 단건 조회 */
    @GetMapping("/api/bus-info/{id}")
    public Result busInfo(@PathVariable final Long id) {

        BusInfo busInfo = busInfoService.findById(id);
        return new Result(new BusInfoDto(busInfo));
    }


    /* 특정 지역 셔틀 버스 정보 조회 */
    @GetMapping("/api/bus-info/area")
    public Result busInfoByArea(
            @RequestBody @Valid final BusInfoByAreaRequest request) {

        List<BusInfoDto> busInfoDtoList = busInfoService.findByArea(request.getArea())
                .stream().map(BusInfoDto::new)
                .collect(Collectors.toList());

        return new Result(busInfoDtoList);
    }


    /* 셔틃 버스 정보 삭제 */
    @DeleteMapping("/api/bus-info/{id}")
    public Result delete(@PathVariable final Long id) {
        busInfoService.delete(id);
        return new Result(new DeleteBusInfoResponse(id));
    }
}