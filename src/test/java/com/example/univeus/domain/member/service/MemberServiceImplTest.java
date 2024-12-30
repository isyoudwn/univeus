package com.example.univeus.domain.member.service;

import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_AUTHORIZED_PHONE;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.Membership;
import com.example.univeus.domain.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member fixureMember;

    @BeforeEach
    void setUp() {
        fixureMember = new Member(
                1L,
                "testEmail",
                "testNickname",
                "testStudentId",
                "testPhoneNumber",
                Gender.MAN,
                Membership.NORMAL,
                Department.ART_AND_PHYSICS
        );
    }

    @Test
    void 유저를_id로_찾는다() {
        // given
        Long memberId = 1L;
        when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(fixureMember));

        // when
        Member findMember = memberService.findById(memberId);

        // then
        assertEquals(memberId, findMember.getId());
    }

    @Test
    void 유저를_email로_찾는다() {
        // given
        String email = "testEmail";
        when(memberRepository.findByEmail(any())).thenReturn(Optional.ofNullable(fixureMember));

        // when
        Member findMember = memberService.findByEmail(email);

        // then
        assertEquals(email, findMember.getEmail());
    }

    @Test
    void 유저가_존재하지_않을경우_유저를_생성하고_리턴한다() {
        // given
        String email = "testEmail";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(fixureMember);

        // when
        Member findMember = memberService.createOrFindMemberByEmail(email);

        // then
        assertEquals(email, findMember.getEmail());
    }

    @Test
    void 유저가_존재할경우_유저를_반환한다() {
        // given
        String email = "testEmail";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.ofNullable(fixureMember));

        // when
        Member findMember = memberService.createOrFindMemberByEmail(email);

        // then
        assertEquals(email, findMember.getEmail());
    }

    @Test
    void 유저를_저장한다() {
        // given
        String email = "testEmail";
        Member member = new Member(
                1L,
                "testEmail",
                null,
                null,
                "testPhoneNumber",
                null,
                null,
                null
        );
        when(memberRepository.save(any())).thenReturn(member);

        // when
        Member savedMember = memberService.saveMember(email);

        // then
        assertEquals(savedMember.getEmail(), email);
    }
}