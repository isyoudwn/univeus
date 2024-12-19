package com.example.univeus.domain.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.example.univeus.common.config.JwtProperties;
import com.example.univeus.common.config.KeyConfig;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.Clock;
import java.time.ZoneId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenProviderTest {
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.system(ZoneId.of("Asia/Seoul"));
        KeyConfig keyConfig = new KeyConfig();
        tokenProvider = new TokenProvider(
                new JwtProperties(
                        60L,
                        60L,
                        "test"),
                clock, keyConfig);
    }


    @Test
    void 토큰을_생성한다() {
        // given
        Long expireMs = 60L;
        String subject = "testSubject";

        // when
        String createdToken = tokenProvider.createToken(subject, expireMs);

        // then
        assertNotNull(createdToken);
    }

    @Test
    void 토큰_검증을_성공한다() {
        // given
        Long expireMs = 600L;
        String subject = "testSubject";
        String createdToken = tokenProvider.createToken(subject, expireMs);

        // when, then
        assertDoesNotThrow(() -> tokenProvider.verify(createdToken));
    }

    @Test
    void 유효시간이_지나면_토큰_검증을_실패한다() {
        // given
        Long expireMs = 0L;
        String subject = "testSubject";
        String createdToken = tokenProvider.createToken(subject, expireMs);

        // when, then
        assertThrows(
                ExpiredJwtException.class,
                () -> tokenProvider.verify(createdToken));
    }

    @Test
    void 유효한_토큰에서_subject를_가져온다() {
        // given
        Long expireMs = 600L;
        String expectedSubject = "testSubject";
        String createdToken = tokenProvider.createToken(expectedSubject, expireMs);

        // when
        String actualSubject = tokenProvider.getSubject(createdToken);

        // then
        Assertions.assertThat(actualSubject).isEqualTo(expectedSubject);
    }
}