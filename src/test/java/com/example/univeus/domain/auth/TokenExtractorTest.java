package com.example.univeus.domain.auth;

import static com.example.univeus.common.exception.ErrorCode.ACCESS_TOKEN_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

import com.example.univeus.domain.auth.exception.TokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenExtractorTest {

    private TokenExtractor tokenExtractor;

    @BeforeEach
    void setUp() {
        tokenExtractor = new TokenExtractor();
    }

    @Test
    void 토큰을_추출한다() {
        // given
        String expectedToken = "Bearer validToken";

        // when
        String actualToken = tokenExtractor.extractToken(expectedToken);

        // then
        assertEquals(expectedToken.substring(7), actualToken);
    }

    @Test
    void 올바르지_않은_헤더_형태이면_토큰_추출을_실패한다() {
        // given
        String expectedToken = "invalidToken";

        // when
        TokenException tokenException = assertThrows(
                TokenException.class, () -> {
                    tokenExtractor.extractToken(expectedToken);
                });

        // then
        assertEquals(ACCESS_TOKEN_NOT_FOUND, tokenException.getErrorCode());
    }

}