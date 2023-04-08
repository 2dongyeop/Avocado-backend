package io.wisoft.capstonedesign.domain.staff.web.dto;

import io.wisoft.capstonedesign.domain.boardreply.web.dto.BoardReplyDto;
import io.wisoft.capstonedesign.domain.healthinfo.web.dto.HealthInfoDto;
import io.wisoft.capstonedesign.domain.staff.persistence.Staff;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffDto {
    private String name;
    private String email;
    private String dept;
    private String hospital;
    private List<BoardReplyDto> boardReplyList;
    private List<HealthInfoDto> healthInfoList;

    public StaffDto(final Staff staff) {
        this.name = staff.getName();
        this.email = staff.getEmail();
        this.dept = String.valueOf(staff.getDept());
        this.hospital = staff.getHospital().getName();

        this.boardReplyList = staff.getBoardReplyList()
                .stream().map(BoardReplyDto::new)
                .collect(Collectors.toList());

        this.healthInfoList = staff.getHealthInfoList()
                .stream().map(HealthInfoDto::new)
                .collect(Collectors.toList());;
    }
}
