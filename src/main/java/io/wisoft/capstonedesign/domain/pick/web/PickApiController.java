package io.wisoft.capstonedesign.domain.pick.web;

import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.pick.application.PickService;
import io.wisoft.capstonedesign.domain.pick.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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


    /* 찜하기 단건 조회 */
    @GetMapping("/api/picks/{id}")
    public Result pick(@PathVariable("id") final Long id) {
        Pick pick = pickService.findById(id);

        return new Result(new PickDto(pick));
    }


    /* 특정 회원의 찜하기 목록 오름차순 조회 */
    @GetMapping("/api/picks/member/{member-id}/create-asc")
    public Result pickByMemberOrderByCreateAsc(@PathVariable("member-id") final Long memberId) {

        List<PickDto> pickDtoList = pickService.findByMemberIdOrderByCreateAsc(memberId)
                .stream().map(PickDto::new)
                .collect(Collectors.toList());

        return new Result(pickDtoList);
    }


    /* 특정 회원의 찜하기 목록 내림차순 조회 */
    @GetMapping("/api/picks/member/{member-id}/create-desc")
    public Result pickByMemberOrderByCreateDesc(@PathVariable("member-id") final Long memberId) {

        List<PickDto> pickDtoList = pickService.findByMemberIdOrderByCreateDesc(memberId)
                .stream().map(PickDto::new)
                .collect(Collectors.toList());

        return new Result(pickDtoList);
    }
}
