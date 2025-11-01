package com.inyro.api.domain.member.repository;

import com.inyro.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySno(String sno);

    void deleteAllBySnoIn(List<String> snoList);

}
