package com.example.univeus.presentation.meeting.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class MeetingRequest {

        public record MeetingPostWriteAndUpdate(
                @NotBlank(message = "제목은 공백이 될 수 없습니다.")
                @Size(min = 5, max = 50, message = "제목의 길이는 1이상 50이하이어야 합니다.")
                String title,

                @NotBlank(message = "본문은 공백이 될 수 없습니다.")
                @Size(min = 5, max = 50, message = "본문의 길이는 1이상 1000이하이어야 합니다.")
                String body,

                @NotBlank(message = "성별 제한은 공백이 될 수 없습니다.")
                String genderLimit,

                @NotBlank(message = "인원수 제한은 공백이 될 수 없습니다.")
                @Pattern(regexp = "^[0-9]+$", message = "인원수 제한은 숫자만 포함해야 합니다.")
                String joinLimit,

                @NotBlank(message = "카테고리는 공백이 될 수 없습니다.")
                String meetingCategory,

                @NotBlank(message = "게시글 마감 일정은 공백이 될 수 없습니다.")
                @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$", message = "게시글 마감 일정은 yyyy-MM-dd'T'HH:mm 형식이어야 합니다.")
                String postDeadline,

                @NotBlank(message = "미팅 일정은 공백이 될 수 없습니다.")
                @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$", message = "미팅 마감 일정은 yyyy-MM-dd'T'HH:mm 형식이어야 합니다.")
                String meetingSchedule,

                @NotBlank(message = "주소는 공백이 될 수 없습니다.")
                String address,

                @NotBlank(message = "위치는 공백이 될 수 없습니다.")
                String latitude,

                @NotBlank(message = "위치는 공백이 될 수 없습니다.")
                String longitude
        ) {
        }
}
