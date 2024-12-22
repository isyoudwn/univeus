package com.example.univeus.domain.auth.service;

import com.example.univeus.domain.auth.repository.RefreshTokenRepository;
import com.example.univeus.domain.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public Boolean isExist(String id) {
        return refreshTokenRepository.findById(id)
                .isPresent();
    }
}
