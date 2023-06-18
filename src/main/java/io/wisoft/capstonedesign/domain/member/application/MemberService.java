package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.member.web.dto.*;
import io.wisoft.capstonedesign.global.exception.ErrorCode;
import io.wisoft.capstonedesign.global.exception.illegal.IllegalValueException;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
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
            throw new IllegalValueException("비밀번호가 일치하지 않아 변경할 수 없습니다.", ErrorCode.ILLEGAL_PASSWORD);
        }
    }


    /**
     * 회원 정보 수정 - 프로필 이미지 or 닉네임
     */
    @Transactional
    public void updateMember(final Long memberId, final String photoPath, final String nickname) {

        final Member member = findById(memberId);

        if (StringUtils.hasText(photoPath)) {
            member.uploadPhotoPath(photoPath);
        }

        if (StringUtils.hasText(nickname)) {
            member.updateNickname(nickname);
        }
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
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 조회 실패"));
    }

    public Member findDetailById(final Long memberId) {
        return memberRepository.findDetailById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 조회 실패"));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
