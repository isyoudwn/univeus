package com.example.univeus.presentation.auth.controller;

import static com.example.univeus.common.response.ResponseMessage.LOGIN_SUCCESS;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_AUTHORIZED_PHONE;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_NOT_AUTHORIZED_PROFILE;
import static com.example.univeus.common.response.ResponseMessage.REFRESH_TOKEN_NOT_FOUND;
import static com.example.univeus.common.response.ResponseMessage.REISSUE_TOKEN_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.univeus.domain.auth.dto.AccessToken;
import com.example.univeus.domain.auth.service.AuthService;
import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.presentation.BaseControllerTest;
import com.example.univeus.presentation.auth.dto.request.AuthRequest;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;


    static ResponseCookie createResponseCookie() {
        return ResponseCookie.from("refresh-token", "refreshTokenValue")
                .maxAge(3600)
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .path("/")
                .build();
    }

    @Nested
    @DisplayName("토큰 재발급 테스트")
    class ReissueTest {

        @Test
        void 토큰_재발급을_성공한다() throws Exception {

            // given
            Cookie cookie = new Cookie("refresh-token", "refreshToken");
            AccessToken accessToken = AccessToken.of("accessTokenValue");
            ResponseTokens responseTokens = ResponseTokens.of(accessToken, createResponseCookie());
            when(authService.reissueTokens(any())).thenReturn(responseTokens);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/auth/reissue")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(cookie)
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(REISSUE_TOKEN_SUCCESS.getMessage()))
                    .andExpect(jsonPath("$.code").value(REISSUE_TOKEN_SUCCESS.getCode()))
                    .andExpect(jsonPath("$.data.accessToken").value("accessTokenValue"));
            ;
        }

        @Test
        void DB에_refresh_token이_없을때_예외를_던진다() throws Exception {
            // given
            Cookie cookie = new Cookie("refresh-token", "refreshToken");
            when(authService.reissueTokens(any())).thenThrow(
                    new MemberException(REFRESH_TOKEN_NOT_FOUND));

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/v1/auth/reissue")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(cookie)
            );

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(REFRESH_TOKEN_NOT_FOUND.getMessage()))
                    .andExpect(jsonPath("$.code").value(REFRESH_TOKEN_NOT_FOUND.getCode()));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class Login {
        @Test
        void 로그인을_성공한다() throws Exception {
            // given
            String googleIdToken = "testGoogleIdToken";
            AccessToken accessToken = AccessToken.of("accessTokenValue");
            ResponseTokens responseTokens = ResponseTokens.of(accessToken, createResponseCookie());
            AuthRequest.Login loginRequest = AuthRequest.Login.of(googleIdToken);
            when(authService.createOrLogin(any())).thenReturn(responseTokens);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)));

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.SET_COOKIE, responseTokens.refreshToken().toString()))
                    .andExpect(jsonPath("$.code").value(LOGIN_SUCCESS.getCode()))
                    .andExpect(jsonPath("$.message").value(LOGIN_SUCCESS.getMessage()))
                    .andExpect(jsonPath("$.data.accessToken").value("accessTokenValue"));
        }

        @Test
        void DB에_존재하지_않는_유저가_로그인_했을_경우_예외가_발생한다() throws Exception {
            // given
            String googleIdToken = "testGoogleIdToken";
            AuthRequest.Login loginRequest = AuthRequest.Login.of(googleIdToken);
            when(authService.createOrLogin(any())).thenThrow(new MemberException(MEMBER_NOT_AUTHORIZED_PHONE));

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(MEMBER_NOT_AUTHORIZED_PHONE.getCode()))
                    .andExpect(jsonPath("$.message").value(MEMBER_NOT_AUTHORIZED_PHONE.getMessage()));
        }

        @Test
        void 전화번호_인증을_마치지_않았을_경우_예외가_발생한다() throws Exception {
            // given
            String googleIdToken = "testGoogleIdToken";
            AuthRequest.Login loginRequest = AuthRequest.Login.of(googleIdToken);
            when(authService.createOrLogin(any())).thenThrow(new MemberException(MEMBER_NOT_AUTHORIZED_PHONE));

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(MEMBER_NOT_AUTHORIZED_PHONE.getCode()))
                    .andExpect(jsonPath("$.message").value(MEMBER_NOT_AUTHORIZED_PHONE.getMessage()));
        }

        @Test
        void 프로필_등록을_마치지_않았을_경우_예외가_발생한다() throws Exception {
            // given
            String googleIdToken = "testGoogleIdToken";
            AuthRequest.Login loginRequest = AuthRequest.Login.of(googleIdToken);
            when(authService.createOrLogin(any())).thenThrow(new MemberException(MEMBER_NOT_AUTHORIZED_PROFILE));

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(MEMBER_NOT_AUTHORIZED_PROFILE.getCode()))
                    .andExpect(jsonPath("$.message").value(MEMBER_NOT_AUTHORIZED_PROFILE.getMessage()));
        }
    }
}
