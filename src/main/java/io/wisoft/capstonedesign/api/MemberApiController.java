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

        List<MemberDto> memberDtoList = memberService.findAll().stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());

        return new Result(memberDtoList);
    }

    /* 회원가입 */
    @PostMapping("/api/members/signup")
    public CreateMemberResponse saveMember(@RequestBody @Valid final CreateMemberRequest request) {

        Member member = Member.newInstance(request.nickname, request.email, request.password, request.phonenumber);
        Long id = memberService.signUp(member);
        return new CreateMemberResponse(id);
    }

    /* 회원 비밀번호 수정 */
    @PatchMapping("/api/members/{member_id}")
    public UpdateMemberResponse updateMemberPassword(
            @PathVariable("member_id") final Long id,
            @RequestBody @Valid final UpdateMemberPasswordRequest request) {

        memberService.updatePassword(id, request.oldPassword, request.newPassword);
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(member.getId());
    }

    /* 회원 닉네임 수정 */
    //TODO 만들기

    /* 회원 프로필사진 업로드 혹은 수정 */
    @PatchMapping("/api/members/{id}")
    public UpdateMemberResponse updateMemberPhotoPath(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberPhotoPathRequest request) {

        memberService.uploadPhotoPath(id, request.photoPath);
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(member.getId());
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

        public MemberDto(final Member member) {
            nickname = member.getNickname();
            email = member.getEmail();
        }
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
    static class UpdateMemberResponse {
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
