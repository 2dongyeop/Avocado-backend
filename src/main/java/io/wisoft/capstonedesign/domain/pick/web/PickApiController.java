package io.wisoft.capstonedesign.domain.pick.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.pick.application.PickService;
import io.wisoft.capstonedesign.domain.pick.web.dto.*;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "찜하기")
@RestController
@RequiredArgsConstructor
public class PickApiController {

    private final PickService pickService;

    @SwaggerApi(summary = "찜하기 생성", implementation = CreatePickResponse.class)
    @SwaggerApiFailWithAuth
    @PostMapping("/api/picks")
    public CreatePickResponse createPick(@RequestBody @Valid final CreatePickRequest request) {
        return new CreatePickResponse(pickService.save(request));
    }


    @SwaggerApi(summary = "찜하기 취소", implementation = DeletePickResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/api/picks/{id}")
    public DeletePickResponse deletePick(@PathVariable("id") final Long id) {
        pickService.cancelPick(id);
        return new DeletePickResponse(id);
    }


    @SwaggerApi(summary = "찜하기 단건 상세 조회", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/api/picks/{id}/details")
    public Result pick(@PathVariable("id") final Long id) {
        return new Result(new PickDto(pickService.findDetailById(id)));
    }
}
