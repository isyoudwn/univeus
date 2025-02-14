package com.example.univeus.domain.auth.dto;


public record UserTokens(AccessToken accessToken, String refreshToken) {
    public static UserTokens of(AccessToken accessToken, String refreshToken) {
        return new UserTokens(accessToken, refreshToken);
    }
}
