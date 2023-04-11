package io.wisoft.capstonedesign.domain.pick.web;

import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.application.PickService;
import io.wisoft.capstonedesign.domain.pick.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PickApiController {

    private final PickService pickService;

    /* 찜하기 생성 */
    @PostMapping("/api/picks/new")
    public CreatePickResponse createPick(@RequestBody @Valid final CreatePickRequest request) {

        Long id = pickService.save(request);
        return new CreatePickResponse(id);
    }


    /* 찜하기 취소 */
    @DeleteMapping("/api/picks/{id}")
    public DeletePickResponse deletePick(@PathVariable("id") final Long id) {
        pickService.cancelPick(id);
        return new DeletePickResponse(id);
    }


    /* 찜하기 단건 상세 조회 */
    @GetMapping("/api/picks/{id}")
    public Result pick(@PathVariable("id") final Long id) {
        Pick pick = pickService.findDetailById(id);

        return new Result(new PickDto(pick));
    }
}
