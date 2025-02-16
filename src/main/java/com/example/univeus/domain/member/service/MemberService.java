package com.example.univeus.domain.member.service;

import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.presentation.member.dto.request.MemberDto;
import java.util.Optional;

public interface MemberService {
    Member findById(Long memberId);

    Member findByEmail(String email);

    Member createOrFindMemberByEmail(String email);

    Member saveMember(String email);

    void updateProfile(Long memberId, Department department, Gender gender, String nickname, String studentId);

    Optional<Member> findByNickname(String nickname);

    void updatePhoneNumber(Long memberId, String phoneNumber);

    void registerProfile(Long memberId, MemberDto.Profile profileRequest);

    void checkNicknameDuplicated(MemberDto.Nickname nicknameRequest);

    void createMember(Member member);
}
