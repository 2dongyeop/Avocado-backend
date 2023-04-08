package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.member.web.dto.*;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EncryptHelper encryptHelper;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * 회원가입
     */
    @Transactional
    public Long signUp(final CreateMemberRequest request) {

        validateDuplicateMember(request);

        Member member = Member.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(encryptHelper.encrypt(request.getPassword1()))
                .phoneNumber(request.getPhonenumber())
                .build();

        memberRepository.save(member);
        return member.getId();
    }

    /** 로그인 */
    public String login(final LoginRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(NullMemberException::new);

        if (!encryptHelper.isMatch(request.getPassword(), member.getPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(member.getNickname());
    }


    private void validateDuplicateMember(final CreateMemberRequest request) {
        List<Member> validateMemberByEmail = memberRepository.findValidateMemberByEmail(request.getEmail());
        List<Member> validateMemberByNickname = memberRepository.findValidateMemberByNickname(request.getNickname());

        if (!validateMemberByEmail.isEmpty() || !validateMemberByNickname.isEmpty()) {
            throw new DuplicateMemberException("중복 회원 발생 : 이미 존재하는 회원입니다.");
        }
    }

    /*
     * 회원 비밀번호 수정
     */
    @Transactional
    public void updatePassword(final Long memberId, final UpdateMemberPasswordRequest request) {

        Member member = findById(memberId);
        validateMemberPassword(member, request);

        member.updatePassword(encryptHelper.encrypt(request.getNewPassword()));
    }

    private void validateMemberPassword(final Member member, final UpdateMemberPasswordRequest request) {

        if (!encryptHelper.isMatch(request.getOldPassword(), member.getPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }


    /* 회원 닉네임 수정 */
    @Transactional
    public void updateMemberNickname(final Long memberId, final UpdateMemberNicknameRequest request) {

        Member member = findById(memberId);
        member.updateNickname(request.getNickname());
    }


    /*
     * 회원 프로필사진 업로드 혹은 수정
     */
    @Transactional
    public void uploadPhotoPath(final Long memberId, final UpdateMemberPhotoPathRequest request) {

        Member member = findById(memberId);
        member.uploadPhotoPath(request.getPhotoPath());
    }


    /* 회원 탈퇴 */
    @Transactional
    public void deleteMember(final Long memberId) {
        Member member = findById(memberId);
        memberRepository.delete(member);
    }


    /*
     * 회원 조회
     */
    public Member findById(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NullMemberException::new);
    }

    public Member findDetailById(final Long memberId) {
        return memberRepository.findDetailById(memberId).orElseThrow(NullMemberException::new);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email).orElseThrow(NullMemberException::new);
    }
}
