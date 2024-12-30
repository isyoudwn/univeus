package com.example.univeus.domain.auth.service;

import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_AUTHORIZED_PHONE;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_AUTHORIZED_PROFILE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.GoogleServerLogin;
import com.example.univeus.domain.auth.GoogleServerLogin.Response;
import com.example.univeus.domain.auth.SocialLogin;
import com.example.univeus.domain.auth.TokenProvider;
import com.example.univeus.domain.auth.dto.AccessToken;
import com.example.univeus.domain.auth.dto.UserTokens;
import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.Membership;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
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
class AuthServiceImplTest {
    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private MemberService memberService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private SocialLogin socialLogin;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenTestService();
        authService = new AuthServiceImpl(refreshTokenService, tokenProvider, memberService, socialLogin);
    }

    @Test
    void 토큰을_재발급_한다() {
        // given
        String tokenValue = "tokenValue";
        when(tokenProvider
                .generateTokens(any()))
                .thenReturn(UserTokens.of
                        (
                                AccessToken.of("testValue"),
                                "refreshTestValue"
                        )
                );
        when(memberService
                .findById(any()))
                .thenReturn(Member.create(
                        "testEmail",
                        "nickName",
                        "studentId",
                        "phoneNumber",
                        Gender.MAN,
                        Membership.NORMAL,
                        Department.ART_AND_PHYSICS));

        // when
        ResponseTokens responseTokens = authService.reissueTokens(tokenValue);

        // then
        assertNotNull(responseTokens);
    }

    @Test
    void 토큰을_발급한다() {
        // given
        Long memberId = 1L;
        when(tokenProvider
                .generateTokens(any()))
                .thenReturn(UserTokens.of
                        (
                                AccessToken.of("testValue"),
                                "refreshTestValue"
                        )
                );
        when(memberService
                .findById(any()))
                .thenReturn(Member.create(
                        "testEmail",
                        "nickName",
                        "studentId",
                        "phoneNumber",
                        Gender.MAN,
                        Membership.NORMAL,
                        Department.ART_AND_PHYSICS));

        // when
        ResponseTokens responseTokens = authService.issueTokens(memberId);

        // then
        assertNotNull(responseTokens);
    }


    @Nested
    @DisplayName("Test createOrLogin")
    class CreateOrLoginTest {
        @Test
        void 유저가_존재하지_않아_생성할_경우_예외를_반환한다() {
            // given
            String googleAccessToken = "testGoogleAccessToken";
            Response response = Response.of(
                    "testId",
                    "test@test.com",
                    true,
                    "testName",
                    "testGivenName",
                    "testFamilyName",
                    "testPicture",
                    "test-locale"
            );
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
            when(socialLogin.getGoogleProfile(googleAccessToken)).thenReturn(response);
            when(memberService.createOrFindMemberByEmail(any())).thenReturn(member);

            // when
            MemberException memberException = assertThrows(MemberException.class, () -> {
                authService.createOrLogin(googleAccessToken);
            });

            // then
            assertEquals(MEMBER_NOT_AUTHORIZED_PHONE, memberException.getResponseMessage());
        }

        @Test
        void 휴대폰_번호인증을_안한_유저는_예외를_발생시킨다() {
            // given
            String googleAccessToken = "testGoogleAccessToken";
            Response response = Response.of(
                    "testId",
                    "test@test.com",
                    true,
                    "testName",
                    "testGivenName",
                    "testFamilyName",
                    "testPicture",
                    "test-locale"
            );

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
            when(socialLogin.getGoogleProfile(googleAccessToken)).thenReturn(response);
            when(memberService.createOrFindMemberByEmail(any())).thenReturn(member);

            // when
            MemberException memberException = assertThrows(MemberException.class, () -> {
                authService.createOrLogin(googleAccessToken);
            });

            // then
            assertEquals(MEMBER_NOT_AUTHORIZED_PHONE, memberException.getResponseMessage());
        }

        @Test
        void 프로필_등록을_하지_않은_유저에_대해_예외를_던진다() {
            // given
            String googleAccessToken = "testGoogleAccessToken";
            Response response = Response.of(
                    "testId",
                    "test@test.com",
                    true,
                    "testName",
                    "testGivenName",
                    "testFamilyName",
                    "testPicture",
                    "test-locale"
            );

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
            when(socialLogin.getGoogleProfile(googleAccessToken)).thenReturn(response);
            when(memberService.createOrFindMemberByEmail(any())).thenReturn(member);

            // when
            MemberException memberException = assertThrows(MemberException.class, () -> {
                authService.createOrLogin(googleAccessToken);
            });

            // then
            assertEquals(MEMBER_NOT_AUTHORIZED_PROFILE, memberException.getResponseMessage());
        }
    }
}