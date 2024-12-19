package com.example.univeus.domain.auth;

import com.example.univeus.common.config.JwtProperties;
import com.example.univeus.common.config.KeyConfig;
import com.example.univeus.common.util.TimeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final Clock clock;
    private final KeyConfig keyConfig;

    public TokenProvider(JwtProperties jwtProperties, Clock clock, KeyConfig keyConfig) {
        this.jwtProperties = jwtProperties;
        this.clock = clock;
        this.keyConfig = keyConfig;
    }

    public String createToken(String subject, Long expireMs) {
        Date now = TimeUtil.localDateTimeToDate(LocalDateTime.now(clock), clock);
        Date expiredTime = new Date(now.getTime() + expireMs);

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(subject)
                .issuedAt(now)
                .signWith(keyConfig.getSecretKey())
                .expiration(expiredTime)
                .compact();
    }

    public Jws<Claims> verify(String token) {
        return Jwts.parser()
                .verifyWith(keyConfig.getSecretKey())
                .build()
                .parseSignedClaims(token);
    }

    public String getSubject(String token) {
        return verify(token)
                .getPayload()
                .getSubject();
    }
}
