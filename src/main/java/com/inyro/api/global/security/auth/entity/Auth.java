package com.inyro.api.global.security.auth.entity;

import com.project.likelion13th_team1.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "auth")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    private Member member;

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
