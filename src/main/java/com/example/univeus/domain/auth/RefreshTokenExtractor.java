package com.example.univeus.domain.auth;

import com.example.univeus.common.exception.ErrorCode;
import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenExtractor {
    private static final String TOKEN_NAME = "refresh-token";
    private final RefreshTokenService refreshTokenService;

    public String extractToken(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .map(Cookie::getValue)
                .filter(refreshTokenService::isExist)
                .findFirst()
                .orElseThrow(() -> new TokenException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }
}
