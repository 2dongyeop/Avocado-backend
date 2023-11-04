package io.wisoft.capstonedesign.domain.staff.application;

import io.wisoft.capstonedesign.domain.board.persistence.Board;
import io.wisoft.capstonedesign.domain.review.persistence.Review;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import io.wisoft.capstonedesign.domain.staff.persistence.StaffMyPageRepository;
import io.wisoft.capstonedesign.global.exception.notfound.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffMyPageService {

    private final StaffMyPageRepository staffMyPageRepository;

    /**
     * 자신이 속한 병원의 리뷰 목록 조회
     */
    public List<Review> findReviewByStaffHospitalName(final Long staffId) {

        final Staff staff = staffMyPageRepository.findById(staffId).orElseThrow(() -> {
            log.info("staffId[{}] not found", staffId);
            return new NotFoundException("의료진 조회 실패");
        });

        final String hospitalName = staff.getHospital().getName();
        return staffMyPageRepository.findReviewListByStaffHospitalName(hospitalName);
    }


    /**
     * 자신이 댓글을 작성한 게시글 목록 조회
     */
    public List<Board> findBoardListByStaffId(final Long staffId) {
        return staffMyPageRepository.findBoardICommentByStaffId(staffId);
    }
}
