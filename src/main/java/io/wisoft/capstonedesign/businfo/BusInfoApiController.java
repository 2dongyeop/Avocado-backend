package io.wisoft.capstonedesign.businfo;

import io.wisoft.capstonedesign.global.enumeration.BusArea;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
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

        BusInfo busInfo = BusInfo.createBusInfo(request.busInfoPath, BusArea.valueOf(request.area));
        Long id = busInfoService.save(busInfo);
        return new CreateBusInfoResponse(id);
    }


    /* 셔틀 버스 단건 조회 */
    @GetMapping("/api/bus-info/{id}")
    public Result busInfo(@PathVariable final Long id) {

        BusInfo busInfo = busInfoService.findOne(id);
        return new Result(new BusInfoDto(busInfo));
    }


    /* 특정 지역 셔틀 버스 정보 조회 */
    @GetMapping("/api/bus-info/area")
    public Result busInfoByArea(
            @RequestBody @Valid final BusInfoByAreaRequest request) {

        List<BusInfoDto> busInfoDtoList = busInfoService.findByArea(request.area)
                .stream().map(BusInfoDto::new)
                .collect(Collectors.toList());

        return new Result(busInfoDtoList);
    }


    /* 셔틃 버스 정보 삭제 */
    @DeleteMapping("/api/bus-info/{id}")
    public Result delete(@PathVariable final Long id) {
        busInfoService.delete(id);
        BusInfo busInfo = busInfoService.findOne(id);
        return new Result(new DeleteBusInfoResponse(busInfo.getId(), busInfo.getStatus().toString()));
    }


    @Data
    static class BusInfoByAreaRequest {
        private String area;
    }


    @Data
    @AllArgsConstructor
    static class DeleteBusInfoResponse {
        private Long id;
        private String status;
    }


    @Data
    @AllArgsConstructor
    static class BusInfoDto {
        private String area;
        private String busInfoPath;

        BusInfoDto(final BusInfo busInfo) {
            area = busInfo.getArea().toString();
            busInfoPath = busInfo.getBusInfoPath();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class CreateBusInfoResponse {
        private Long id;
    }

    @Data
    static class CreateBusInfoRequest {
        private String busInfoPath;
        private String area;
    }
}