package com.example.univeus.presentation.auth.dto.response;

import com.example.univeus.domain.auth.dto.AccessToken;
import org.springframework.http.ResponseCookie;

public class AuthResponse {

    public record ResponseTokens(AccessToken accessToken, ResponseCookie refreshToken) {
        public static ResponseTokens of(AccessToken accessToken, ResponseCookie refreshToken) {
            return new ResponseTokens(accessToken, refreshToken);
        }
    }
}
