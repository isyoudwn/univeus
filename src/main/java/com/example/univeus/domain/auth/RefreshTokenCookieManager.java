package com.example.univeus.domain.auth;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieManager {
    private static final String TOKEN_NAME = "refresh-token";
    private final RefreshTokenService refreshTokenService;
    private static final Long COOKIE_MAX_AGE = ((60 * 60) * 24) * 14L;

    public String extractToken(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .map(Cookie::getValue)
                .filter(refreshTokenService::isExist)
                .findFirst()
                .orElseThrow(() -> new TokenException(ResponseMessage.REFRESH_TOKEN_NOT_FOUND));
    }

    public ResponseCookie createCookie(String tokenValue) {
        return ResponseCookie.from(TOKEN_NAME, tokenValue)
                .maxAge(COOKIE_MAX_AGE)
                .secure(true)
                .httpOnly(true)
                .sameSite("none")
                .path("/")
                .build();
    }
}
