package com.example.univeus.presentation.auth.controller;

import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.common.annotation.Auth;
import com.example.univeus.common.annotation.MemberOnly;
import com.example.univeus.common.response.Response;
import com.example.univeus.domain.auth.dto.AccessToken;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.auth.service.AuthService;
import com.example.univeus.presentation.auth.dto.request.AuthRequest;
import com.example.univeus.presentation.auth.dto.request.AuthRequest.Login;
import com.example.univeus.presentation.auth.dto.request.AuthRequest.Nickname;
import com.example.univeus.presentation.auth.dto.request.AuthRequest.Profile;
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

    @MemberOnly
    @PostMapping("/profile")
    public ResponseEntity<Response<String>> registerProfile(
            @Auth Accessor accessor,
            @Valid @RequestBody Profile profileRequest
    ) {
        authService.registerProfile(accessor.getMemberId(), profileRequest);
        return ResponseEntity
                .ok()
                .body(Response.success(
                        PROFILE_REGISTER_SUCCESS.getCode(),
                        PROFILE_REGISTER_SUCCESS.getMessage()
                ));
    }

    @PostMapping("/nickname/duplicated")
    public ResponseEntity<Response<String>> checkNicknameDuplicated(
            @Valid @RequestBody Nickname nicknameRequest
    ) {
        authService.checkNicknameDuplicated(nicknameRequest);
        return ResponseEntity
                .ok()
                .body(Response.success(
                        CHECK_NICKNAME_DUPLICATED_SUCCESS.getCode(),
                        CHECK_NICKNAME_DUPLICATED_SUCCESS.getMessage()
                ));
    }
}
