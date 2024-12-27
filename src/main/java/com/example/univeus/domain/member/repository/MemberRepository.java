package com.example.univeus.domain.member.repository;

import com.example.univeus.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
