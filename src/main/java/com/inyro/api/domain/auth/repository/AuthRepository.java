package com.inyro.api.domain.auth.repository;

import com.inyro.api.domain.auth.entity.Auth;
import com.inyro.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByMember(Member member);
}
