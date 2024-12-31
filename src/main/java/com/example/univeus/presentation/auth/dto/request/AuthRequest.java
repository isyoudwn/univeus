package com.example.univeus.presentation.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

    public record Login(
            @NotBlank(message = "ID 토큰은 공백이 될 수 없습니다.")
            String googleIdToken
    ) {
        public static Login of(String googleIdToken) {
            return new Login(googleIdToken);
        }
    }

    public record Profile(
            @NotBlank(message = "닉네임은 공백이 될 수 없습니다.")
            @Size(min = 1, max = 30, message = "닉네임의 길이는 1이상 30이하입니다.")
            String nickname,

            @NotBlank(message = "학과는 공백이 될 수 없습니다.")
            String department,

            @NotBlank(message = "성별은 공백이 될 수 없습니다.")
            String gender,

            @NotBlank(message = "학번은 공백이 될 수 없습니다.")
            @Size(min = 1, max = 30, message = "학번의 길이는 1이상 30이하입니다.")
            String studentId
    ) {
        public static Profile of(
                String nickname, String department, String gender, String studentId) {
            return new Profile(nickname, department, gender, studentId);
        }
    }

    public record Nickname(
            @NotBlank(message = "닉네임은 공백이 될 수 없습니다.")
            String nickname
    ) {
        public static Nickname of(String nickname) {
            return new Nickname(nickname);
        }
    }
}
