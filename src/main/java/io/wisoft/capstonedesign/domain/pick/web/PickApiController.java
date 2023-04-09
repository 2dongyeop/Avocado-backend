package io.wisoft.capstonedesign.domain.pick.web;

import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.application.PickService;
import io.wisoft.capstonedesign.domain.pick.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        Pick pick = pickService.findById(id);

        return new DeletePickResponse(pick.getId(), pick.getStatus().toString());
    }


    /* 찜하기 단건 상세 조회 */
    @GetMapping("/api/picks/{id}")
    public Result pick(@PathVariable("id") final Long id) {
        Pick pick = pickService.findDetailById(id);

        return new Result(new PickDto(pick));
    }


    /** 특정 회원의 찜하기 목록 페이징 조회 */
    @GetMapping("/api/picks/member/{member-id}")
    public Page<PickDto> pickByMemberOrderByCreateAsc(
            @PathVariable("member-id") final Long memberId, final Pageable pageable) {

        return pickService.findByMemberIdUsingPaging(memberId, pageable)
                .map(PickDto::new);
    }
}
