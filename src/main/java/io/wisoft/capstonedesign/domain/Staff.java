package io.wisoft.capstonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Staff {

    @Id @GeneratedValue
    @Column(name = "staff_id")
    private Long id;

    @Column(name = "staff_name", nullable = false)
    private String name;

    @Column(name = "staff_email" , unique = true, nullable = false)
    private String email;

    @Column(name = "staff_password", nullable = false)
    private String password;

    @Column(name = "staff_phonenumber", nullable = false)
    private String phoneNumber;

    @Column(name = "staff_license_path", nullable = false)
    private String license_path;

    @Column(name = "dept", nullable = false)
    private String dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "staff")
    private final List<BoardReply> boardReplyList = new ArrayList<>();
}
