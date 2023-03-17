package io.wisoft.capstonedesign.api;

import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    /* 회원 조회 */
    @GetMapping("/api/members")
    public Result members() {

        List<Member> memberList = memberService.findAll();

        List<MemberDto> memberDtoList = memberList.stream()
                .map(member -> new MemberDto(member.getNickname(), member.getEmail()))
                .collect(Collectors.toList());

        return new Result(memberDtoList);
    }


    /* 회원가입 */
    @PostMapping("/api/members")
    public CreateMemberResponse saveMember(
            @RequestBody @Valid CreateMemberRequest request) {

        Member member = Member.newInstance(request.nickname, request.email, request.password, request.phonenumber);

        Long id = memberService.signUp(member);
        return new CreateMemberResponse(id);
    }


    /* 회원 비밀번호 수정 */
    @PatchMapping("/api/members/{id}")
    public UpdateMemberRequest updateMemberPassword(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberPasswordRequest request) {

        memberService.updatePassword(id, request.oldPassword, request.newPassword);
        Member member = memberService.findOne(id);

        return new UpdateMemberRequest(member.getId());
    }

    /* 회원 프로필사진 업로드 혹은 수정 */
    @PostMapping("/api/members/{id}")
    public UpdateMemberRequest updateMemberPhotoPath(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberPhotoPathRequest request) {

        memberService.uploadPhotoPath(id, request.photoPath);
        Member member = memberService.findOne(id);

        return new UpdateMemberRequest(member.getId());
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String nickname;
        private String email;
    }


    @Data
    static class UpdateMemberPasswordRequest {
        private String oldPassword;
        private String newPassword;
    }

    @Data
    static class UpdateMemberPhotoPathRequest {
        private String photoPath;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberRequest {
        private Long id;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class CreateMemberRequest {
        private String nickname;
        private String email;
        private String password;
        private String phonenumber;
    }
}
