package com.example.univeus.presentation.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    public record PhoneNumber(
            @NotBlank(message = "휴대폰 번호는 공백이 될 수 없습니다.")
            @Size(min = 9, max = 11, message = "휴대폰 번호의 길이가 올바르지 않습니다.")
            @Pattern(regexp = "^[0-9]+$", message = "휴대폰 번호는 숫자만 포함해야 합니다.")
            String phoneNumber
    ) {
        public static PhoneNumber of(String phoneNumber) {
            return new PhoneNumber(phoneNumber);
        }
    }

    public record Certification(
            @NotBlank(message = "휴대폰 번호는 공백이 될 수 없습니다.")
            @Size(min = 9, max = 11, message = "휴대폰 번호의 길이가 올바르지 않습니다.")
            @Pattern(regexp = "^[0-9]+$", message = "휴대폰 번호는 숫자만 포함해야 합니다.")
            String phoneNumber,

            @NotBlank(message = "인증번호는 공백이 될 수 없습니다.")
            @Pattern(regexp = "\\d{6}", message = "인증번호는 숫자 6자리여야 합니다.")
            String code
    ) {
        public static Certification of(String phoneNumber, String code) {
            return new Certification(phoneNumber, code);
        }
    }
}
