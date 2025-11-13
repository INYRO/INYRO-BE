package com.inyro.api.domain.auth.entity;

import com.inyro.api.domain.auth.exception.AuthErrorCode;
import com.inyro.api.domain.auth.exception.AuthException;
import com.inyro.api.domain.member.entity.Member;
import com.inyro.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "auth")
public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sno", nullable = false)
    private String sno;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void validateNotSamePassword(String newPassword, PasswordEncoder encoder) {
        if (encoder.matches(newPassword, this.password)) {
            throw new AuthException(AuthErrorCode.NEW_PASSWORD_IS_CURRENT_PASSWORD);
        }
    }

    public void resetPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
