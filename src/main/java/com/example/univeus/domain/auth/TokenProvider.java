package com.example.univeus.domain.auth;

import com.example.univeus.common.config.JwtProperties;
import com.example.univeus.common.util.TimeUtil;
import com.example.univeus.domain.auth.dto.AccessToken;
import com.example.univeus.domain.auth.dto.UserTokens;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final String EMPTY_SUBJECT = "";

    private final JwtProperties jwtProperties;
    private final Clock clock;
    private final SecretKey secretKey;

    public UserTokens generateTokens(String subject) {
        String accessToken = createToken(subject, jwtProperties.getAccessExpireMs());
        String refreshToken = createToken(EMPTY_SUBJECT, jwtProperties.getRefreshExpireMs());

        return UserTokens.of(
                AccessToken.of(accessToken),
                refreshToken
        );
    }

    public String createToken(String subject, Long expireMs) {
        Date now = TimeUtil.localDateTimeToDate(LocalDateTime.now(clock), clock);
        Date expiredTime = new Date(now.getTime() + expireMs);

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(subject)
                .issuedAt(now)
                .signWith(secretKey)
                .expiration(expiredTime)
                .compact();
    }

    public Jws<Claims> verify(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    public String getSubject(String token) {
        return verify(token)
                .getPayload()
                .getSubject();
    }
}
