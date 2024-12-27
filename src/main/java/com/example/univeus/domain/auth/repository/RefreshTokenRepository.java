package com.example.univeus.domain.auth.repository;

import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByMember(Member member);

    Member findByMember(Member member);
}
