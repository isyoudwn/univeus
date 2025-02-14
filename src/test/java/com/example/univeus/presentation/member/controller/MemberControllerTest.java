package com.example.univeus.presentation.member.controller;

import static com.example.univeus.common.response.ResponseMessage.CHECK_NICKNAME_DUPLICATED_SUCCESS;
import static com.example.univeus.common.response.ResponseMessage.PROFILE_REGISTER_SUCCESS;
import static com.example.univeus.common.response.ResponseMessage.UPDATE_PHONE_NUMBER_SUCCESS;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.example.univeus.common.resolver.AuthenticationResolver;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.BaseControllerTest;
import com.example.univeus.presentation.member.dto.request.MemberDto;
import com.example.univeus.presentation.member.dto.request.MemberDto.PhoneNumber;
import com.example.univeus.presentation.member.dto.request.MemberDto.Profile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Accessor accessor;

    @MockBean
    private AuthenticationResolver authenticationResolver;

    @MockBean
    private MemberService memberService;


    @BeforeEach
    void setup() {
        accessor = Accessor.member(1L);

        when(authenticationResolver.supportsParameter(any())).thenReturn(true);
        when(authenticationResolver.resolveArgument(any(), any(), any(), any())).thenReturn(accessor);
    }

    @Nested
    @DisplayName("휴대폰 번호 업데이트 테스트")
    class TestUpdatePhoneNumber {

        @Test
        void 휴대폰_번호_업데이트에_성공한다() throws Exception {
            // given
            MemberDto.PhoneNumber phoneNumberRequest = PhoneNumber.of("01012345678");

            // when
            ResultActions resultActions = mockMvc.perform(patch("/api/v1/member/phone")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(phoneNumberRequest)));

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.code").value(UPDATE_PHONE_NUMBER_SUCCESS.getCode()))
                    .andExpect(jsonPath("$.message").value(UPDATE_PHONE_NUMBER_SUCCESS.getMessage()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "010123467889", "test12345"})
        void 올바르지_않은_요청을_할_경우_예외를_반환한다(String phoneNumber) throws Exception {
            // given
            MemberDto.PhoneNumber phoneNumberRequest = MemberDto.PhoneNumber.of(phoneNumber);

            // when
            ResultActions resultActions = mockMvc.perform(patch("/api/v1/member/phone")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(phoneNumberRequest)));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(false))
                    .andExpect(jsonPath("$.code").value("FORMAT-001"))
                    .andExpect(jsonPath("$.message",
                            anyOf(
                                    is("휴대폰 번호는 공백이 될 수 없습니다."),
                                    is("휴대폰 번호의 길이가 올바르지 않습니다."),
                                    is("휴대폰 번호는 숫자만 포함해야 합니다."))));
        }
    }

    @Nested
    @DisplayName("프로필 등록 테스트")
    class TestRegisterProfile {

        @Test
        void 올바른_형식으로_요청을_보낼경우_예외가_발생하지_않는다() throws Exception {

            MemberDto.Profile profile = Profile.of(
                    "nickname",
                    "department",
                    "gender",
                    "studentId"
            );

            ResultActions resultActions =
                    mockMvc.perform(post("/api/v1/member/profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(profile)));

            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(("$.code")).value(PROFILE_REGISTER_SUCCESS.getCode()))
                    .andExpect(jsonPath(("$.message")).value(PROFILE_REGISTER_SUCCESS.getMessage()));
        }

        @ParameterizedTest
        @MethodSource("provideProfiles")
        void 올바르지_않은_프로필_등록_요청을_보낼경우_예외가_발생한다(
                String nickname, String department, String gender, String studentId
        ) throws Exception {

            MemberDto.Profile profile = Profile.of(
                    nickname,
                    department,
                    gender,
                    studentId
            );

            ResultActions resultActions =
                    mockMvc.perform(post("/api/v1/member/profile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(profile)));

            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath(("$.code")).value("FORMAT-001"));
        }

        private static Stream<Arguments> provideProfiles() {
            return Stream.of(
                    Arguments.of("", "department", "gender", "studentId"),       // 닉네임이 공백
                    Arguments.of("nickname", "", "gender", "studentId"),        // 학과가 공백
                    Arguments.of("nickname", "department", "", "studentId"),    // 성별이 공백
                    Arguments.of("nickname", "department", "gender", "")        // 학번이 공백
            );
        }

        @Nested
        @DisplayName("닉네임 중복 테스트")
        class TestCheckNicknameDuplicated {

            @Test
            void 올바른_형식으로_요청을_보낼경우_예외가_발생하지_않는다() throws Exception {

                // given
                MemberDto.Nickname nicknameRequest = MemberDto.Nickname.of("testNickname");

                // when
                ResultActions resultActions =
                        mockMvc
                                .perform(post("/api/v1/member/nickname/duplicated")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(nicknameRequest)));

                // then
                resultActions
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.code").value(CHECK_NICKNAME_DUPLICATED_SUCCESS.getCode()))
                        .andExpect(jsonPath("$.message").value(CHECK_NICKNAME_DUPLICATED_SUCCESS.getMessage()));
            }


            @ParameterizedTest
            @ValueSource(strings = {"", " ", "    "})
            void 닉네임이_올바른_입력이_아닐경우_예외를_던진다(String nickname) throws Exception {

                // given
                MemberDto.Nickname nicknameRequest = MemberDto.Nickname.of(nickname);

                // when
                ResultActions resultActions =
                        mockMvc
                                .perform(post("/api/v1/member/nickname/duplicated")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(nicknameRequest)));

                // then
                resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(false))
                        .andExpect(jsonPath("$.code").value("FORMAT-001"))
                        .andExpect(jsonPath("$.message").value("닉네임은 공백이 될 수 없습니다."));
            }
        }
    }
}
