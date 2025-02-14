package com.example.univeus.domain.auth.service;

import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.model.Member;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {
    ResponseCookie createResponseToken(String tokenValue);

    RefreshToken save(String tokenValue, Member member);

    RefreshToken findById(String refreshToken);

    void delete(RefreshToken refreshToken);
}
