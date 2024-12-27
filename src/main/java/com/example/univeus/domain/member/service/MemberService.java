package com.example.univeus.domain.member.service;

import com.example.univeus.domain.member.model.Member;

public interface MemberService {
    Member findById(Long memberId);
}
