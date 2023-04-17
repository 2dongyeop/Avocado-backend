package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.member.web.dto.*;
import io.wisoft.capstonedesign.global.exception.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullMemberException;
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

    /*
     * 회원 비밀번호 수정
     */
    @Transactional
    public void updatePassword(final Long memberId, final UpdateMemberPasswordRequest request) {

        final Member member = findById(memberId);
        validateMemberPassword(member, request);

        member.updatePassword(encryptHelper.encrypt(request.newPassword()));
    }

    private void validateMemberPassword(final Member member, final UpdateMemberPasswordRequest request) {

        if (!encryptHelper.isMatch(request.oldPassword(), member.getPassword())) {
            throw new IllegalValueException("비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }


    /* 회원 닉네임 수정 */
    @Transactional
    public void updateMemberNickname(final Long memberId, final UpdateMemberNicknameRequest request) {

        final Member member = findById(memberId);
        member.updateNickname(request.nickname());
    }


    /*
     * 회원 프로필사진 업로드 혹은 수정
     */
    @Transactional
    public void uploadPhotoPath(final Long memberId, final UpdateMemberPhotoPathRequest request) {

        final Member member = findById(memberId);
        member.uploadPhotoPath(request.photoPath());
    }


    /* 회원 탈퇴 */
    @Transactional
    public void deleteMember(final Long memberId) {
        final Member member = findById(memberId);
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
}
