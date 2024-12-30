package com.example.univeus.presentation.auth.dto.request;

public class AuthRequest {

    public record Login(String googleIdToken) {

        public static Login of(String googleIdToken) {
            return new Login(googleIdToken);
        }
    }
}
