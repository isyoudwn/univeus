package com.example.univeus.common.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class JwtProperties {

    @Value("${jwt.expire-ms}")
    private final Long accessExpireMs;

    @Value("${jwt.expire-ms}")
    private final Long refreshExpireMs;

    @Value("${jwt.issuer}")
    private final String issuer;
}