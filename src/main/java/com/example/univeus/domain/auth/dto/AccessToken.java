package com.example.univeus.domain.auth.dto;

public record AccessToken(String accessToken) {
    public static AccessToken of(String accessToken) {
        return new AccessToken(accessToken);
    }
}
