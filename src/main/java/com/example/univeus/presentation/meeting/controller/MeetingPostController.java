package com.example.univeus.presentation.meeting.controller;

import static com.example.univeus.presentation.meeting.dto.request.MeetingRequest.*;

import com.example.univeus.common.annotation.Auth;
import com.example.univeus.common.annotation.MemberOnly;
import com.example.univeus.common.response.Response;
import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.meeting.service.MeetingPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingPostController {

    private final MeetingPostService meetingPostService;

    @MemberOnly

    @PostMapping("/post")
    public ResponseEntity<Response<String>> writeMeetingPost(
            @Auth Accessor accessor,
            @Valid @RequestBody WriteMeetingPost writeMeetingPost
    ) {
        Long memberId = accessor.getMemberId();
        meetingPostService.writePost(memberId, writeMeetingPost);
        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.MEETING_UPLOAD_SUCCESS.getCode(),
                        ResponseMessage.MEETING_UPLOAD_SUCCESS.getMessage()
                ));
    }
}
