package com.example.univeus.common.config;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KeyConfig {
    private final SecretKey secretKey;

    public KeyConfig() {
        this.secretKey = Jwts.SIG.HS256.key().build();
    }
}
