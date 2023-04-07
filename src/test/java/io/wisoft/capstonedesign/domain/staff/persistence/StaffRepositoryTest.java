package io.wisoft.capstonedesign.domain.staff.persistence;

import io.wisoft.capstonedesign.domain.review.persistence.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StaffRepositoryTest {

    @Autowired StaffRepository staffRepository;

    @Test
    public void findReviewListByStaffHospitalName() throws Exception {
        //given -- 조건
        String targetHospital = "서울대병원";

        //when -- 동작
        List<Review> list = staffRepository.findReviewListByStaffHospitalName(targetHospital);

        //then -- 검증
        Assertions.assertThat(list.size()).isEqualTo(1);
    }
}