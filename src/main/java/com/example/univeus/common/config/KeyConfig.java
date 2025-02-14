package com.example.univeus.common.config;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KeyConfig {
    @Bean
    public SecretKey secretKey() {
        return Jwts.SIG.HS256.key().build();
    }
}
