package com.example.univeus.domain.auth.service;

import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;

public interface AuthService {
    ResponseTokens reissueTokens(String refreshToken);

    ResponseTokens issueTokens(Long memberId);
}
