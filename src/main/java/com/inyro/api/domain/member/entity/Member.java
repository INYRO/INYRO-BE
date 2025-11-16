package com.inyro.api.domain.member.entity;

import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.common.entity.BaseEntity;
import com.inyro.api.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public void linkAuth(Auth auth) {
        this.auth = auth;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
