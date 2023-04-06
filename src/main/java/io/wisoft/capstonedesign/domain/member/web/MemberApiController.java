package io.wisoft.capstonedesign.domain.member.web;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.web.dto.*;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /* 회원 단건 조회 */
    @GetMapping("/api/members/{id}")
    public Result member(@PathVariable("id") final Long id) {

        Member member = memberService.findOne(id);

        return new Result(new MemberDto(member.getNickname(), member.getEmail(), member.getPhoneNumber()));
    }

    /* 회원 목록 조회 */
    @GetMapping("/api/members")
    public Result members() {

        List<MemberListDto> memberListDtoList = memberService.findAll().stream()
                .map(MemberListDto::new)
                .collect(Collectors.toList());

        return new Result(memberListDtoList);
    }


    /** 회원가입 */
    @PostMapping("/api/members/signup")
    public CreateMemberResponse signup(@RequestBody @Valid final CreateMemberRequest request) {

        if (!request.getPassword1().equals(request.getPassword2())) {
            throw new IllegalValueException("두 비밀번호 값이 일치하지 않습니다.");
        }

        Long id = memberService.signUp(request);
        return new CreateMemberResponse(id);
    }


    /** test api
     * TODO 지우기 */
    @GetMapping("/api/info/{member-email}")
    public ResponseEntity<MemberResponse> getUserFromToken(
            @PathVariable("member-email") final String email) {

        final Member member = memberService.findByEmail(email);
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @Getter
    static class MemberResponse {
        private String nickname;
        public static MemberResponse of(final Member member) {
            MemberResponse memberResponse = new MemberResponse();
            memberResponse.nickname = member.getNickname();
            return memberResponse;
        }
    }


    /** 로그인 */
    @PostMapping("/api/members/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid final LoginRequest request) {

        String token = memberService.createToken(request);
        return ResponseEntity.ok(new TokenResponse(token, "bearer"));
    }


    /* 회원 비밀번호 수정 */
    @PatchMapping("/api/members/{id}/password")
    public UpdateMemberResponse updateMemberPassword(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberPasswordRequest request) {

        memberService.updatePassword(id, request);
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(member.getId());
    }

    /* 회원 닉네임 수정 */
    @PatchMapping("/api/members/{id}/nickname")
    public UpdateMemberResponse updateMemberNickname(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberNicknameRequest request) {

        memberService.updateMemberNickname(id, request);
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(member.getId());
    }


    /* 회원 프로필사진 업로드 혹은 수정 */
    @PatchMapping("/api/members/{id}/photo")
    public UpdateMemberResponse updateMemberPhotoPath(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final UpdateMemberPhotoPathRequest request) {

        memberService.uploadPhotoPath(id, request);
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(member.getId());
    }


    /* 회원 탈퇴 */
    @DeleteMapping("/api/members/{id}")
    public DeleteMemberResponse deleteMember(@PathVariable("id") final Long id) {

        memberService.deleteMember(id);
        return new DeleteMemberResponse(id);
    }
}
