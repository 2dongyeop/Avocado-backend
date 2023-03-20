package io.wisoft.capstonedesign.api;

import io.wisoft.capstonedesign.domain.Pick;
import io.wisoft.capstonedesign.service.PickService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
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

        Long id = pickService.save(request.memberId, request.hospitalId);
        return new CreatePickResponse(id);
    }


    /* 찜하기 취소 */
    @DeleteMapping("/api/picks/{id}")
    public DeletePickResponse deletePick(@PathVariable("id") final Long id) {
        pickService.cancelPick(id);
        Pick pick = pickService.findOne(id);

        return new DeletePickResponse(pick.getId(), pick.getStatus().toString());
    }


    /* 찜하기 단건 조회 */
    @GetMapping("/api/picks/{id}")
    public Result pick(@PathVariable("id") final Long id) {
        Pick pick = pickService.findOne(id);

        return new Result(new PickDto(pick));
    }


    /* 특정 회원의 찜하기 목록 조회 */
    @GetMapping("/api/picks/member/{member-id}")
    public Result pickByMember(@PathVariable("member-id") final Long memberId) {

        List<PickDto> pickDtoList = pickService.findByMemberId(memberId)
                .stream().map(PickDto::new)
                .collect(Collectors.toList());

        return new Result(pickDtoList);
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

    @Data
    static class PickDto {
        private String name;
        private String hospital;

        public PickDto(Pick pick) {
            this.name = pick.getMember().getNickname();
            this.hospital = pick.getHospital().getName();
        }
    }

    @Data
    @AllArgsConstructor
    static class DeletePickResponse {
        private Long id;
        private String status;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class CreatePickRequest {
        private Long memberId;
        private Long hospitalId;
    }

    @Data
    @AllArgsConstructor
    static class CreatePickResponse {
        private Long id;
    }
}
