package com.example.univeus.domain.member.model;

import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_AUTHORIZED_PHONE;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_AUTHORIZED_PROFILE;
import static org.junit.jupiter.api.Assertions.*;

import com.example.univeus.domain.member.exception.MemberException;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void 멤버의_정보가_모두_차있으면_예외가_발생하지_않는다() {

        // given
        Member member = new Member(
                1L,
                "testEmail",
                "testNickname",
                "testStudentId",
                "testPhoneNumber",
                Gender.MAN,
                Membership.NORMAL,
                Department.ART_AND_PHYSICS
        );

        // when, then
        assertDoesNotThrow(member::checkProceed);
    }

    @Test
    void 멤버의_프로필완성_진행상황이_휴대폰번호_인증_미완료_상태라면_예외를_던진다() {
        // given
        Member member = new Member(
                1L,
                "testEmail",
                null,
                null,
                null,
                null,
                null,
                null
        );

        // when
        MemberException memberException = assertThrows(MemberException.class, member::checkProceed);

        // then
        assertEquals(MEMBER_NOT_AUTHORIZED_PHONE, memberException.getResponseMessage());
    }

    @Test
    void 멤버의_프로필완성_진행상황이_프로필_등록_미완료_상태라면_예외를_던진다() {
        // given
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

        // when
        MemberException memberException = assertThrows(MemberException.class, member::checkProceed);

        // then
        assertEquals(MEMBER_NOT_AUTHORIZED_PROFILE, memberException.getResponseMessage());
    }
}
