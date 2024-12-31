package com.example.univeus.domain.auth.service;

import com.example.univeus.presentation.auth.dto.request.AuthRequest.Nickname;
import com.example.univeus.presentation.auth.dto.request.AuthRequest.Profile;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;

public interface AuthService {
    ResponseTokens reissueTokens(String refreshToken);

    ResponseTokens issueTokens(Long memberId);

    ResponseTokens createOrLogin(String uri);

    void registerProfile(Long memberId, Profile profileRequest);

    void checkNicknameDuplicated(Nickname nicknameRequest);
}
