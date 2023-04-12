package io.wisoft.capstonedesign.domain.member.web;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.web.dto.*;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /* 회원 단건 조회 */
    @GetMapping("/api/members/{id}/details")
    public Result member(@PathVariable("id") final Long id) {

        Member member = memberService.findDetailById(id);

        return new Result(new MemberDto(member));
    }

    /* 회원 목록 조회 */
    @GetMapping("/api/members")
    public Result members() {

        List<MemberListDto> memberListDtoList = memberService.findAll().stream()
                .map(MemberListDto::new)
                .collect(Collectors.toList());

        return new Result(memberListDtoList);
    }


    /* 회원 비밀번호 수정 */
    @PatchMapping("/api/members/{id}/password")
    public UpdateMemberResponse updateMemberPassword(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberPasswordRequest request) {

        memberService.updatePassword(id, request);
        Member member = memberService.findById(id);

        return new UpdateMemberResponse(member.getId());
    }

    /* 회원 닉네임 수정 */
    @PatchMapping("/api/members/{id}/nickname")
    public UpdateMemberResponse updateMemberNickname(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberNicknameRequest request) {

        memberService.updateMemberNickname(id, request);
        Member member = memberService.findById(id);

        return new UpdateMemberResponse(member.getId());
    }


    /* 회원 프로필사진 업로드 혹은 수정 */
    @PatchMapping("/api/members/{id}/photo")
    public UpdateMemberResponse updateMemberPhotoPath(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberPhotoPathRequest request) {

        memberService.uploadPhotoPath(id, request);
        Member member = memberService.findById(id);

        return new UpdateMemberResponse(member.getId());
    }


    /* 회원 탈퇴 */
    @DeleteMapping("/api/members/{id}")
    public DeleteMemberResponse deleteMember(@PathVariable("id") final Long id) {

        memberService.deleteMember(id);
        return new DeleteMemberResponse(id);
    }
}
