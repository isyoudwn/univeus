package com.example.univeus.domain.member.service;

import static com.example.univeus.common.response.ResponseMessage.DEPARTMENT_NOT_FOUND;
import static com.example.univeus.common.response.ResponseMessage.GENDER_NOT_FOUND;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_NICKNAME_DUPLICATED;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.Membership;
import com.example.univeus.domain.member.repository.MemberRepository;
import com.example.univeus.presentation.member.dto.request.MemberRequest.Nickname;
import com.example.univeus.presentation.member.dto.request.MemberRequest.Profile;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("test updateProfile")
    class TestUpdateProfile {
        @Test
        void 프로필_업데이트를_성공한다() {
            // given
            Long memberId = 1L;
            Department department = Department.ART_AND_PHYSICS;
            Gender gender = Gender.MAN;
            String nickname = "testNickname";
            String studentId = "testStudentId";
            when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(fixureMember));

            // when, then
            assertDoesNotThrow(() ->
                    memberService.updateProfile(memberId, department, gender, nickname, studentId)
            );
        }

        @Test
        void 프로필_업데이트를_실패한다() {
            // given
            Long memberId = 1L;
            Department department = Department.ART_AND_PHYSICS;
            Gender gender = Gender.MAN;
            String nickname = "testNickname";
            String studentId = "testStudentId";
            when(memberRepository.findById(any())).thenReturn(Optional.empty());

            // when
            MemberException memberException = assertThrows(MemberException.class, () -> {
                memberService.updateProfile(memberId, department, gender, nickname, studentId);
            });

            // then
            assertEquals(MEMBER_NOT_FOUND, memberException.getResponseMessage());
        }
    }

    @Nested
    @DisplayName("test findByNickname")
    class TestFindByNickname {

        @Test
        void 닉네임으로_조회를_성공한다() {
            // given
            String nickname = "testNickname";
            when(memberRepository.findByNickname(any())).thenReturn(Optional.ofNullable(fixureMember));

            // when
            Optional<Member> member = memberService.findByNickname(nickname);

            // then
            boolean present = member.isPresent();
            assertTrue(present);
        }

        @Test
        void 닉네임으로_조회를_실패한다() {
            // given
            String nickname = "testNickname";
            when(memberRepository.findByNickname(any())).thenReturn(Optional.empty());

            // when
            Optional<Member> member = memberService.findByNickname(nickname);

            // then
            boolean present = member.isPresent();
            assertFalse(present);
        }
    }

    @Nested
    @DisplayName("test updatePhoneNumber")
    class TestUpdatePhoneNumber {

        @Test
        void 전화번호_업데이트를_성공한다() {
            // given
            Long memberId = 1L;
            String newPhoneNumber = "01022222222";
            when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(fixureMember));

            // when, then
            assertDoesNotThrow(() -> {
                memberService.updatePhoneNumber(memberId, newPhoneNumber);
            });
        }

        @Test
        void 존재하지_않는_사용자일_경우_전화번호_업데이트를_실패한다() {
            // given
            Long memberId = 1L;
            String newPhoneNumber = "01022222222";
            when(memberRepository.findById(any())).thenReturn(Optional.empty());

            // when
            MemberException memberException = assertThrows(MemberException.class, () ->
                    memberService.updatePhoneNumber(memberId, newPhoneNumber)
            );

            // then
            assertEquals(MEMBER_NOT_FOUND.getCode(), memberException.getCode());
            assertEquals(MEMBER_NOT_FOUND.getMessage(), memberException.getMessage());
        }
    }

    @Nested
    @DisplayName("프로필을 등록한다")
    class TestRegisterProfile {

        @Test
        void 프로필_등록을_성공한다() {
            // given
            Long memberId = 1L;
            Profile profile = Profile.of(
                    "testNickname",
                    "인문대학",
                    "WOMAN",
                    "testStudentId"
            );
            when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(fixureMember));

            // when
            memberService.registerProfile(memberId, profile);

            // then
            assertDoesNotThrow(() ->
                    memberService.registerProfile(memberId, profile));
        }

        @Test
        void 성별이_올바르지_않게_입력될_경우_프로필_등록을_실패한다() {
            // given
            Long memberId = 1L;
            Profile profile = Profile.of(
                    "testNickname",
                    "인문대학",
                    "WO",
                    "testStudentId"
            );

            // when
            MemberException memberException = assertThrows(
                    MemberException.class, () ->
                            memberService.registerProfile(memberId, profile));

            // then
            assertEquals(GENDER_NOT_FOUND, memberException.getResponseMessage());
        }

        @Test
        void 학과가_올바르지_않게_입력될_경우_프로필_등록을_실패한다() {
            // given
            Long memberId = 1L;
            Profile profile = Profile.of(
                    "testNickname",
                    "인문",
                    "WOMAN",
                    "testStudentId"
            );

            // when
            MemberException memberException = assertThrows(
                    MemberException.class, () ->
                            memberService.registerProfile(memberId, profile));

            // then
            assertEquals(DEPARTMENT_NOT_FOUND, memberException.getResponseMessage());
        }
    }

    @Nested
    @DisplayName("닉네임 중복체크를 수행한다.")
    class TestNicknameDuplicated {

        Member member = new Member(
                1L,
                "testEmail",
                "testNickName",
                null,
                "testPhoneNumber",
                null,
                null,
                null
        );

        @Test
        void 닉네임이_중복되지_않는다() {
            // given
            Nickname nicknameRequest = Nickname.of("newNickname");
            when(memberService.findByNickname(any())).thenReturn(Optional.empty());



            // when then
            assertDoesNotThrow(() ->
                    memberService.checkNicknameDuplicated(nicknameRequest)
            );
        }

        @Test
        void 닉네임이_중복된다면_예외를_던진다() {
            // given
            Nickname nicknameRequest = Nickname.of("testNickname");
            when(memberService.findByNickname(any())).thenReturn(Optional.ofNullable(member));

            // when
            MemberException memberException = assertThrows(MemberException.class,
                    () -> memberService.checkNicknameDuplicated(nicknameRequest)
            );

            // then
            Assertions.assertEquals(MEMBER_NICKNAME_DUPLICATED, memberException.getResponseMessage());
        }
    }
}
