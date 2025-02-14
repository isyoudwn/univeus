package com.example.univeus.domain.auth;

import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.auth.GoogleServerLogin.Response;
import com.example.univeus.domain.auth.exception.AuthException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SocialLogin {

    public GoogleServerLogin.Response getGoogleProfile(String googleAccessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(googleAccessToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return Response.of(
                    claims.getSubject(),
                    claims.getStringClaim("email"),
                    claims.getBooleanClaim("email_verified"),
                    claims.getStringClaim("name"),
                    claims.getStringClaim("given_name"),
                    claims.getStringClaim("family_name"),
                    claims.getStringClaim("picture"),
                    claims.getStringClaim("locale")
            );
        } catch (ParseException e) {
            throw new AuthException(LOGIN_FAIL);
        }
    }
}
