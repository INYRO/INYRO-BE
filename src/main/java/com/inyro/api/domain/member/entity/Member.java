package com.inyro.api.domain.member.entity;

import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.member.exception.MemberException;
import com.inyro.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sno", nullable = false)
    private String sno;

    @Column(name = "dept", nullable = false)
    private String dept;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "member",cascade = CascadeType.ALL)
    private Auth auth;

    public void linkAuth(Auth auth) {
        this.auth = auth;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
