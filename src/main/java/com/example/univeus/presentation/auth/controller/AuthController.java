package com.example.univeus.presentation.auth.controller;

import static com.example.univeus.common.response.ResponseMessage.*;
import static com.example.univeus.presentation.auth.dto.request.AuthRequest.*;

import com.example.univeus.common.annotation.Auth;
import com.example.univeus.common.annotation.MemberOnly;
import com.example.univeus.common.response.Response;
import com.example.univeus.domain.auth.dto.AccessToken;
import com.example.univeus.domain.auth.service.AuthService;
import com.example.univeus.domain.auth.service.SmsCertificationService;
import com.example.univeus.presentation.auth.dto.request.AuthRequest.Login;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;
import jakarta.validation.Valid;
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
    private final SmsCertificationService smsCertificationService;

    @GetMapping("/reissue")
    public ResponseEntity<Response<AccessToken>> reissueTokens(
            @CookieValue("refresh-token") String refreshToken
    ) {
        ResponseTokens responseTokens = authService.reissueTokens(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseTokens.refreshToken().toString())
                .body(Response.success(
                        REISSUE_TOKEN_SUCCESS.getCode(),
                        REISSUE_TOKEN_SUCCESS.getMessage(),
                        responseTokens.accessToken()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<AccessToken>> login(
            @Valid @RequestBody Login loginRequest
    ) {
        String googleIdToken = loginRequest.googleIdToken();
        // TODO : 로그인 중 throw 해도 body 에 토큰 주기 추가
        ResponseTokens loginResult = authService.createOrLogin(googleIdToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.refreshToken().toString())
                .body(Response.success(
                        LOGIN_SUCCESS.getCode(),
                        LOGIN_SUCCESS.getMessage(),
                        loginResult.accessToken()
                ));
    }

    @PostMapping("number/request")
    public ResponseEntity<Response<String>> getCertificationNumber(
            @Valid @RequestBody PhoneNumber phoneNumberRequest
    ) {
        String phoneNumber = phoneNumberRequest.phoneNumber();
        smsCertificationService.issueCertificationSms(phoneNumber);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        CERTIFICATION_REQUEST_SUCCESS.getCode(),
                        CERTIFICATION_REQUEST_SUCCESS.getMessage()
                ));
    }

    @PostMapping("number/verify")
    public ResponseEntity<Response<String>> verifyCertificationNumber(
            @Valid @RequestBody Certification certificationRequest
    ) {
        String code = certificationRequest.code();
        String phoneNumber = certificationRequest.phoneNumber();

        smsCertificationService.verifyNumber(code, phoneNumber);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        CERTIFICATION_VERIFY_SUCCESS.getCode(),
                        CERTIFICATION_VERIFY_SUCCESS.getMessage()
                ));
    }
}
