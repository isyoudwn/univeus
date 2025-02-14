package com.example.univeus.common;

import com.example.univeus.domain.auth.RefreshTokenCookieManager;
import com.example.univeus.domain.auth.TokenExtractor;
import com.example.univeus.domain.auth.TokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestAuthConfig {
    @MockBean
    public TokenProvider jwtProvider;

    @MockBean
    public RefreshTokenCookieManager refreshTokenCookieManager;

    @MockBean
    public TokenExtractor accessTokenExtractor;
}

