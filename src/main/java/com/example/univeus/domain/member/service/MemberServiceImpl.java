package com.example.univeus.domain.member.service;

import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public Member createOrFindMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseGet(() ->
                        memberRepository.save(Member.createByEmail(email)));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public Member saveMember(String email) {
        Member member = Member.createByEmail(email);
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateProfile(Long memberId, Department department, Gender gender, String nickName, String studentId) {
        Member member = findById(memberId);
        member.updateProfile(department, gender, nickName, studentId, null);
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    @Override
    @Transactional
    public void updatePhoneNumber(Long memberId, String phoneNumber) {
        Member member = findById(memberId);
        member.updateProfile(
                member.getDepartment(),
                member.getGender(),
                member.getNickname(),
                member.getStudentId(),
                phoneNumber
        );
    }
}
