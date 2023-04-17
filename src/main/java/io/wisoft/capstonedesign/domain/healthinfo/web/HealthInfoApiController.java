package io.wisoft.capstonedesign.domain.healthinfo.web;

import io.wisoft.capstonedesign.domain.healthinfo.application.HealthInfoService;
import io.wisoft.capstonedesign.domain.healthinfo.persistence.HealthInfo;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class HealthInfoApiController {

    private final HealthInfoService healthInfoService;


    /* 건강 정보 등록 */
    @PostMapping("/api/health-infos/new")
    public CreateHealthInfoResponse createHealthInfo(
            @RequestBody @Valid final CreateHealthInfoRequest request) {

        final Long id = healthInfoService.save(request);
        final HealthInfo healthInfo = healthInfoService.findById(id);
        return new CreateHealthInfoResponse(healthInfo.getId());
    }


    /* 건강 정보 삭제 */
    @DeleteMapping("/api/health-infos/{id}")
    public DeleteHealthInfoResponse deleteHealthInfo(@PathVariable("id") final Long id) {
        healthInfoService.delete(id);
        return new DeleteHealthInfoResponse(id);
    }


    /* 건강 정보 단건 조회 */
    @GetMapping("/api/health-infos/{id}/details")
    public Result healthInfo(@PathVariable("id") final Long id) {
        return new Result(new HealthInfoDto(healthInfoService.findDetailById(id)));
    }


    /**
     * 특정 병과의 건강 정보 목록 페이징 조회
     * ex) /api/health-infos/department?page=0&size=5&sort=createAt,desc
     */
    @GetMapping("/api/health-infos/department")
    public Page<HealthInfoDto> healthInfosByDepartmentUsingPaging(
            @RequestBody @Valid final HealthInfoByDepartmentRequest request, final Pageable pageable) {

        return healthInfoService.findAllByDeptUsingPaging(request.getDepartment(), pageable)
                .map(HealthInfoDto::new);
    }


    /**
     * 건강정보 목록을 페이지별로 조회하기
     * ex) /api/health-infos?page=0&size=5&sort=createAt,desc
     */
    @GetMapping("/api/health-infos")
    public Page<HealthInfoDto> healthInfosUsingPaging(final Pageable pageable) {
        return healthInfoService.findByUsingPaging(pageable).map(HealthInfoDto::new);
    }
}
