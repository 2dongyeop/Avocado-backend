package io.wisoft.capstonedesign.service;

import io.wisoft.capstonedesign.domain.Member;
import io.wisoft.capstonedesign.exception.IllegalValueException;
import io.wisoft.capstonedesign.exception.duplicate.DuplicateMemberException;
import io.wisoft.capstonedesign.exception.nullcheck.NullMemberException;
import io.wisoft.capstonedesign.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long signUp(Member member) {

        //comment: 회원 중복 검증
        validateDuplicateMember(member);
        memberRepository.signUp(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembersByEmail = memberRepository.findByEmail(member.getEmail());
        List<Member> findMembersByNickname = memberRepository.findByNickname(member.getNickname());
        if (findMembersByEmail.size() != 0 || findMembersByNickname.size() != 0) {
            throw new DuplicateMemberException("중복 회원 발생 : 이미 존재하는 회원입니다.");
        }
    }

    /*
     * 회원 비밀번호 수정
     */
    @Transactional
    public void updatePassword(Long memberId, String oldPassword ,String newPassword) {

        Member member = findOne(memberId);
        validateMemberPassword(member, oldPassword);

        member.updatePassword(newPassword);
    }

    private void validateMemberPassword(Member member, String oldPassword) {

        if (!member.getPassword().equals(oldPassword)) {
            throw new IllegalValueException("비밀번호가 일치하지 않아 변경할 수 없습니다.");
        }
    }


    /*
     * 회원 프로필사진 업로드 혹은 수정
     */
    @Transactional
    public void uploadPhotoPath(Long memberId, String newPhotoPath) {

        Member member = findOne(memberId);
        member.uploadPhotoPath(newPhotoPath);
    }


    /*
     * 회원 조회
     */
    public Member findOne(Long memberId) {

        Member getMember = memberRepository.findOne(memberId);
        if (getMember == null) {
            throw new NullMemberException("해당 회원 정보가 존재하지 않습니다.");
        }
        return getMember;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public List<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
