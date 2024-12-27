package com.example.univeus.domain.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.univeus.domain.auth.TokenProvider;
import com.example.univeus.domain.auth.dto.AccessToken;
import com.example.univeus.domain.auth.dto.UserTokens;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.Membership;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;
import org.junit.jupiter.api.BeforeEach;
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

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenTestService();
        authService = new AuthServiceImpl(refreshTokenService, tokenProvider, memberService);
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
}