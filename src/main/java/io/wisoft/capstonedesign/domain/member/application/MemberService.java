package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.member.web.dto.CreateMemberRequest;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberNicknameRequest;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberPasswordRequest;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberPhotoPathRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
     * 회원가입
     */
    @Transactional
    public Long signUp(final CreateMemberRequest request) {

        Member member = Member.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhonenumber())
                .build();

        //comment: 회원 중복 검증
        validateDuplicateMember(member);
        memberRepository.signUp(member);
        return member.getId();
    }

    private void validateDuplicateMember(final Member member) {
        List<Member> findMembersByEmail = memberRepository.findByEmail(member.getEmail());
        List<Member> findMembersByNickname = memberRepository.findByNickname(member.getNickname());

        if (!findMembersByEmail.isEmpty() || !findMembersByNickname.isEmpty()) {
            throw new DuplicateMemberException("중복 회원 발생 : 이미 존재하는 회원입니다.");
        }
    }

    /*
     * 회원 비밀번호 수정
     */
    @Transactional
    public void updatePassword(final Long memberId, final UpdateMemberPasswordRequest request) {

        Member member = findOne(memberId);
        validateMemberPassword(member, request);

        member.updatePassword(request.getNewPassword());
    }

    private void validateMemberPassword(final Member member, final UpdateMemberPasswordRequest request) {

        if (!member.getPassword().equals(request.getOldPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }


    /* 회원 닉네임 수정 */
    @Transactional
    public void updateMemberNickname(final Long memberId, final UpdateMemberNicknameRequest request) {

        Member member = findOne(memberId);
        member.updateNickname(request.getNickname());
    }


    /*
     * 회원 프로필사진 업로드 혹은 수정
     */
    @Transactional
    public void uploadPhotoPath(final Long memberId, final UpdateMemberPhotoPathRequest request) {

        Member member = findOne(memberId);
        member.uploadPhotoPath(request.getPhotoPath());
    }


    /* 회원 탈퇴 */
    @Transactional
    public void deleteMember(final Long memberId) {
        memberRepository.deleteMember(memberId);
    }


    /*
     * 회원 조회
     */
    public Member findOne(final Long memberId) {
        return memberRepository.findOne(memberId).orElseThrow(NullMemberException::new);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<Member> findByNickname(final String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public List<Member> findByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }
}
