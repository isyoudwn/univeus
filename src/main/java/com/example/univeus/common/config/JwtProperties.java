package com.example.univeus.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtProperties {

    @Value("${jwt.access-expire-ms}")
    private Long accessExpireMs;

    @Value("${jwt.refresh-expire-ms}")
    private Long refreshExpireMs;

    @Value("${jwt.issuer}")
    private String issuer;
}
