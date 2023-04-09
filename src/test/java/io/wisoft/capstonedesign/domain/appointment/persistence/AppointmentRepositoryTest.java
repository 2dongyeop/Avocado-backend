package io.wisoft.capstonedesign.domain.appointment.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AppointmentRepositoryTest {

    @Autowired AppointmentRepository appointmentRepository;

    @Test
    public void paging() throws Exception {
        //given -- 조건
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createAt"));

        //when -- 동작
        Page<Appointment> page = appointmentRepository.findByMemberIdUsingPaging(1L, request);
        List<Appointment> content = page.getContent();

        //then -- 검증
        assertThat(content.size()).isEqualTo(1); //콘텐트 사이즈
        assertThat(page.getTotalElements()).isEqualTo(1); //요소개수
        assertThat(page.getNumber()).isEqualTo(0);  //페이지번호
        assertThat(page.getTotalPages()).isEqualTo(1); //총 페이지 수
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isFalse();
    }
}