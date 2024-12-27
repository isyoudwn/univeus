package com.example.univeus.domain.auth.service;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.RefreshTokenCookieManager;
import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.auth.repository.RefreshTokenRepository;
import com.example.univeus.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenCookieManager cookieManager;

    @Override
    public Boolean isExist(String id) {
        return refreshTokenRepository.findById(id)
                .isPresent();
    }

    @Override
    public RefreshToken findById(String tokenValue) {
        return refreshTokenRepository.findById(tokenValue)
                .orElseThrow(() -> new TokenException(ResponseMessage.REFRESH_TOKEN_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    @Override
    public ResponseCookie createResponseToken(String tokenValue) {
        return cookieManager.createCookie(tokenValue);
    }

    @Override
    @Transactional
    public RefreshToken save(String tokenValue, Member member) {
        RefreshToken refreshToken = RefreshToken.create(tokenValue, member);
        return refreshTokenRepository.save(refreshToken);
    }
}
