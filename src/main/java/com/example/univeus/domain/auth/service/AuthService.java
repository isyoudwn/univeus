package com.example.univeus.domain.auth.service;

import com.example.univeus.domain.auth.model.RefreshToken;

public interface AuthService {
    void saveRefreshToken(RefreshToken refreshToken);
}
