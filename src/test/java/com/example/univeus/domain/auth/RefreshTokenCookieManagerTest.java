package com.example.univeus.domain.auth;

import static com.example.univeus.common.response.ResponseMessage.REFRESH_TOKEN_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

@ExtendWith(MockitoExtension.class)
class RefreshTokenCookieManagerTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenCookieManager refreshTokenCookieManager;

    @Test
    void 유효한_토큰을_추출한다() {
        // given
        Cookie[] cookies = {
                new Cookie("test", "testDummy"),
                new Cookie("refresh-token", "validToken"),
        };
        when(refreshTokenRepository.existsById(any())).thenReturn(true);

        // when
        String actualTokenValue = refreshTokenCookieManager.extractToken(cookies);

        // then
        assertEquals("validToken", actualTokenValue);
    }

    @Test
    void 존재하지_않는_토큰이라면_예외를_반환한다() {
        // given
        Cookie[] cookies = {
                new Cookie("test", "testDummy"),
                new Cookie("refresh-token", "notExist")
        };
        when(refreshTokenRepository.existsById(any())).thenReturn(false);

        // when
        TokenException tokenException = assertThrows(TokenException.class, () -> {
            refreshTokenCookieManager.extractToken(cookies);
        });

        // then
        assertEquals(REFRESH_TOKEN_NOT_FOUND, tokenException.getResponseMessage());
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
            refreshTokenCookieManager.extractToken(cookies);
        });

        // then
        assertEquals(REFRESH_TOKEN_NOT_FOUND, tokenException.getResponseMessage());
    }

    @Test
    void ResponseCookie를_생성한다() {
        // given
        String cookieValue = "testCookieValue";
        Long COOKIE_MAX_AGE = ((60 * 60) * 24) * 14L;

        // when
        ResponseCookie responseCookie = refreshTokenCookieManager.createCookie(cookieValue);

        // then
        assertEquals("/", responseCookie.getPath());
        assertEquals("none", responseCookie.getSameSite());
        assertEquals(COOKIE_MAX_AGE, responseCookie.getMaxAge().getSeconds());
        assertTrue(responseCookie.isHttpOnly());
        assertTrue(responseCookie.isSecure());
    }
}