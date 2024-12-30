package com.example.univeus.domain.member.service;

import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.domain.member.repository.MemberRepository;
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
    public Member createOrFindMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> saveMember(email));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    @Override
    public Member saveMember(String email) {
        Member member = Member.createByEmail(email);
        return memberRepository.save(member);
    }
}
