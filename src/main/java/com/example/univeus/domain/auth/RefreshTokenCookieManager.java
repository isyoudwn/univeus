package com.example.univeus.domain.auth;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.repository.RefreshTokenRepository;
import com.example.univeus.domain.auth.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieManager {
    private static final String TOKEN_NAME = "refresh-token";
    private final RefreshTokenRepository refreshTokenRepository;
    private static final Duration COOKIE_MAX_AGE = Duration.ofDays(14L);

    public String extractToken(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .filter(cookie -> refreshTokenRepository.existsById(cookie.getValue()))
                .findFirst()
                .orElseThrow(() -> new TokenException(ResponseMessage.REFRESH_TOKEN_NOT_FOUND))
                .getValue();
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
