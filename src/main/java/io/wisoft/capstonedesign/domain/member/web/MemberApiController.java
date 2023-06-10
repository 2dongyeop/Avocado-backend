package io.wisoft.capstonedesign.domain.member.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.web.dto.*;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApi;
import io.wisoft.capstonedesign.global.annotation.swagger.SwaggerApiFailWithAuth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "회원")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @SwaggerApi(summary = "회원 단건 조회", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @GetMapping("/{id}/details")
    public Result member(@PathVariable("id") final Long id) {
        return new Result(new MemberDto(memberService.findDetailById(id)));
    }

    @SwaggerApi(summary = "회원 목록 조회", implementation = Result.class)
    @SwaggerApiFailWithAuth
    @GetMapping
    public Result members() {
        return new Result(memberService.findAll().stream()
                .map(MemberListDto::new)
                .collect(Collectors.toList()));
    }


    @SwaggerApi(summary = "회원 비밀번호 변경", implementation = UpdateMemberResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}/password")
    public UpdateMemberResponse updateMemberPassword(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberPasswordRequest request) {

        memberService.updatePassword(id, request);
        final Member member = memberService.findById(id);

        return new UpdateMemberResponse(member.getId());
    }


    @SwaggerApi(summary = "회원 정보 수정", implementation = UpdateMemberResponse.class)
    @SwaggerApiFailWithAuth
    @PatchMapping("/{id}")
    public UpdateMemberResponse updateMember(
            @PathVariable("id") final Long id,
            @RequestParam(value = "photoPath", required = false) final String photoPath,
            @RequestParam(value = "nickname", required = false) final String nickname) {

        memberService.updateMember(id, photoPath, nickname);
        final Member member = memberService.findById(id);

        return new UpdateMemberResponse(member.getId());
    }



    @SwaggerApi(summary = "회원 탈퇴", implementation = DeleteMemberResponse.class)
    @SwaggerApiFailWithAuth
    @DeleteMapping("/{id}")
    public DeleteMemberResponse deleteMember(@PathVariable("id") final Long id) {
        memberService.deleteMember(id);
        return new DeleteMemberResponse(id);
    }
}
