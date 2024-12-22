package com.example.univeus.domain.auth;

import static com.example.univeus.common.exception.ErrorCode.*;

import com.example.univeus.domain.auth.exception.TokenException;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {
    private static final String TYPE = "Bearer ";

    public String extractToken(String headerValue) {
        if (headerValue == null || !headerValue.startsWith(TYPE)) {
            throw new TokenException(ACCESS_TOKEN_NOT_FOUND);
        }
        return headerValue.substring(TYPE.length());
    }
}
