package io.wisoft.capstonedesign.domain.staff.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffMyPageRepository;
import io.wisoft.capstonedesign.global.exception.nullcheck.NullStaffException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffMyPageService {

    private final StaffMyPageRepository staffMyPageRepository;

    /**
     * 자신이 속한 병원의 리뷰 목록 조회
     */
    public List<Review> findReviewByStaffHospitalName(final Long staffId) {

        Staff staff = staffMyPageRepository.findById(staffId)
                .orElseThrow(NullStaffException::new);

        String hospitalName = staff.getHospital().getName();
        return staffMyPageRepository.findReviewListByStaffHospitalName(hospitalName);
    }


    /**
     * 자신이 댓글을 작성한 게시글 목록 조회
     */
    public List<Board> findBoardListByStaffId(final Long staffId) {
        return staffMyPageRepository.findBoardICommentByStaffId(staffId);
    }
}
