package com.example.univeus.presentation.auth.controller;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.common.response.Response;
import com.example.univeus.domain.auth.dto.AccessToken;
import com.example.univeus.domain.auth.service.AuthService;
import com.example.univeus.presentation.auth.dto.request.AuthRequest;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/reissue")
    public ResponseEntity<Response<AccessToken>> reissueTokens(
            @CookieValue("refresh-token") String refreshToken) {
        ResponseTokens responseTokens = authService.reissueTokens(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseTokens.refreshToken().toString())
                .body(Response.success(
                        ResponseMessage.REISSUE_TOKEN_SUCCESS.getCode(),
                        ResponseMessage.REISSUE_TOKEN_SUCCESS.getMessage(),
                        responseTokens.accessToken()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<AccessToken>> login(
            @RequestBody AuthRequest.Login loginRequest
    ) {
        String googleIdToken = loginRequest.googleIdToken();
        ResponseTokens loginResult = authService.createOrLogin(googleIdToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.refreshToken().toString())
                .body(Response.success(
                        ResponseMessage.LOGIN_SUCCESS.getCode(),
                        ResponseMessage.LOGIN_SUCCESS.getMessage(),
                        loginResult.accessToken()
                ));
    }
}
