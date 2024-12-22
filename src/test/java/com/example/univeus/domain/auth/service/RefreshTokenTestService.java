package com.example.univeus.domain.auth.service;

public class RefreshTokenTestService implements RefreshTokenService {
    @Override
    public Boolean isExist(String id) {
        return !id.equals("notExist");
    }
}
