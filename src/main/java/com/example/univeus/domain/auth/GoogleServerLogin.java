package com.example.univeus.domain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpHeaders;

public class GoogleServerLogin {

    public record Request(String uri,
                          String clientId,
                          String clientSecret,
                          String redirectUri,
                          HttpHeaders headers
    ) {
        public static Request of(String uri, String clientId, String clientSecret, String redirectUri,
                                 HttpHeaders headers) {
            return new Request(uri, clientId, clientSecret, redirectUri, headers);
        }
    }

    public record Response(
            String id,
            String email,
            Boolean verifiedEmail,
            String name,
            String givenName,
            String familyName,
            String picture,
            String locale
    ) {
        public static Response of(String id, String email, Boolean verifiedEmail, String name, String givenName,
                                  String familyName,
                                  String picture, String locale) {
            return new Response(id, email, verifiedEmail, name, givenName, familyName, picture, locale);
        }
    }
}
