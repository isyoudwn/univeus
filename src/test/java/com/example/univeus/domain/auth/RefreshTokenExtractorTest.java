package com.example.univeus.domain.auth;

import static com.example.univeus.common.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.service.RefreshTokenService;
import com.example.univeus.domain.auth.service.RefreshTokenTestService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefreshTokenExtractorTest {
    private RefreshTokenExtractor refreshTokenExtractor;

    @BeforeEach
    void setUp() {
        RefreshTokenService refreshTokenService = new RefreshTokenTestService();
        refreshTokenExtractor = new RefreshTokenExtractor(refreshTokenService);
    }

    @Test
    void 유효한_토큰을_추출한다() {
        // given
        Cookie[] cookies = {
                new Cookie("test", "testDummy"),
                new Cookie("refresh-token", "validToken"),
        };

        // when
        String actualTokenValue = refreshTokenExtractor.extractToken(cookies);

        // then
        Assertions.assertEquals("validToken", actualTokenValue);
    }

    @Test
    void 존재하지_않는_토큰이라면_예외를_반환한다() {
        // given
        Cookie[] cookies = {
                new Cookie("test", "testDummy"),
                new Cookie("refresh-token", "notExist")
        };

        // when
        TokenException tokenException = assertThrows(TokenException.class, () -> {
            refreshTokenExtractor.extractToken(cookies);
        });

        // then
        assertEquals(REFRESH_TOKEN_NOT_FOUND, tokenException.getErrorCode());
    }

    @Test
    void cookie에_토큰이_존재하지_않는다면_예외를_반환한다() {
        // given
        Cookie[] cookies = {
                new Cookie("test", "testDummy"),
                new Cookie("test2", "notExist")
        };

        // when
        TokenException tokenException = assertThrows(TokenException.class, () -> {
            refreshTokenExtractor.extractToken(cookies);
        });

        // then
        assertEquals(REFRESH_TOKEN_NOT_FOUND, tokenException.getErrorCode());
    }
}