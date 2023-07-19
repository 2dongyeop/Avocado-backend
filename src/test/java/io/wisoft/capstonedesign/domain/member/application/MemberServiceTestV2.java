package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.domain.member.persistence.Member;
import io.wisoft.capstonedesign.domain.member.persistence.MemberRepository;
import io.wisoft.capstonedesign.domain.member.web.dto.UpdateMemberPasswordRequest;
import io.wisoft.capstonedesign.global.config.bcrypt.EncryptHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTestV2 {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EncryptHelper encryptHelper;


    @Nested
    @DisplayName("회원 비밀번호 수정")
    class UpdateMemberPassword {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 비밀번호가 수정되어야 한다.")
        void 성공() throws Exception {

            //given
            final Long memberId = 1L;

            final Member member = Member.newInstance(
                    "회원비밀번호수정성공",
                    "회원비밀번호수정성공@email.com",
                    "pass12",
                    "phoneNumber"
            );
            final var request = new UpdateMemberPasswordRequest(
                    "pass12",
                    "newPass12"
            );

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(encryptHelper.isMatch(any(), any())).willReturn(true);


            //when
            memberService.updatePassword(memberId, request);

            //then
            Assertions.assertThat(member.getUpdatedAt()).isAfter(member.getCreatedAt());
        }
    }
    
    
    @Nested
    @DisplayName("회원 정보(프로필 이미지 or 닉네임) 수정")
    class UpdateMember {
        
        @Test
        @DisplayName("요청이 성공적으로 수행되어, 회원 정보가 수정되어야 한다.")
        void 성공() throws Exception {

            //given
            final Long memberId = 1L;

            final Member member = Member.newInstance(
                    "회원정보수정성공",
                    "회원정보수정성공@email.com",
                    "pass12",
                    "phoneNumber"
            );

            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            //when
            memberService.updateMember(memberId, "newPhoto", "newNickname");
            
            //then
            Assertions.assertThat(member.getMemberPhotoPath()).isEqualTo("newPhoto");
            Assertions.assertThat(member.getNickname()).isEqualTo("newNickname");
        }
    }


    @Nested
    @DisplayName("회원 탈퇴")
    class DeleteMember {

        @Test
        @DisplayName("요청이 성공적으로 수행되어, 회원이 탈퇴되어야 한다.")
        void 성공() throws Exception {

            //given
            final Long memberId = 1L;

            final Member member = Member.newInstance(
                    "회원탈퇴성공",
                    "회원탈퇴성공@email.com",
                    "pass12",
                    "phoneNumber"
            );
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

            //when
            memberService.deleteMember(memberId);

            //then
            verify(memberRepository, times(1)).delete(any());
        }
    }
}
