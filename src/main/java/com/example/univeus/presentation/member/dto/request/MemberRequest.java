package com.example.univeus.presentation.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberRequest {
    public record PhoneNumber(
            @NotBlank(message = "휴대폰 번호는 공백이 될 수 없습니다.")
            @Size(min = 8, max = 11, message = "휴대폰 번호의 길이가 올바르지 않습니다.")
            @Pattern(regexp = "^[0-9]+$", message = "휴대폰 번호는 숫자만 포함해야 합니다.")
            String phoneNumber
    ) {
        public static PhoneNumber of(String phoneNumber) {
            return new PhoneNumber(phoneNumber);
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
