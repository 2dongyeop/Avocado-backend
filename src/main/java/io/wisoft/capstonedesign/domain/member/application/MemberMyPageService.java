package io.wisoft.capstonedesign.domain.member.application;

import io.wisoft.capstonedesign.domain.appointment.persistence.Appointment;
import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.member.persistence.MemberMyPageRepository;
import io.wisoft.capstonedesign.domain.pick.persistence.Pick;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberMyPageService {

    private final MemberMyPageRepository memberMyPageRepository;

    /**
     * 자신이 쓴 리뷰 목록 페이징 조회
     */
    public Page<Review> findReviewsByMemberIdUsingPaging(final Long memberId, final Pageable pageable) {
        return memberMyPageRepository.findReviewsByMemberIdUsingPaging(memberId, pageable);
    }

    /**
     * 자신이 쓴 게시글 목록 페이징 조회
     */
    public Page<Board> findBoardsByMemberIdUsingPaging(final Long memberId, final Pageable pageable) {
        return memberMyPageRepository.findBoardsByMemberIdUsingPaging(memberId, pageable);
    }

    /**
     * 자신의 예약 정보 목록 조회
     */
    public List<Appointment> findAppointmentsByMemberId(final Long memberId) {
        return memberMyPageRepository.findAppointmentsByMemberId(memberId);
    }

    /**
     * 자신이 찜한 병원 목록 페이징 조회
     */
    public Page<Pick> findPicksByMemberIdUsingPaging(final Long memberId, final Pageable pageable) {
        return memberMyPageRepository.findPicksByMemberIdUsingPaging(memberId, pageable);
    }
}
