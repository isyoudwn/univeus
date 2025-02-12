package com.example.univeus.presentation.participant.controller;


import static com.example.univeus.common.response.ResponseMessage.PARTICIPANT_SUCCESS;
import static com.example.univeus.common.response.ResponseMessage.REMOVE_PARTICIPANT_SUCCESS;

import com.example.univeus.common.annotation.Auth;
import com.example.univeus.common.annotation.MemberOnly;
import com.example.univeus.common.response.Response;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @MemberOnly
    @PostMapping("/{postId}")
    public ResponseEntity<Response<String>> participate(
            @Auth Accessor accessor,
            @PathVariable String postId
    ) {
        Long memberId = accessor.getMemberId();
        participantService.participate(Long.valueOf(postId), memberId);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        PARTICIPANT_SUCCESS.getCode(),
                        PARTICIPANT_SUCCESS.getMessage()
                ));
    }

    @MemberOnly
    @DeleteMapping("/{postId}")
    public ResponseEntity<Response<String>> leaveMeeting(
            @Auth Accessor accessor,
            @PathVariable String postId
    ) {
        Long memberId = accessor.getMemberId();
        participantService.removeParticipant(Long.valueOf(postId), memberId);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        REMOVE_PARTICIPANT_SUCCESS.getCode(),
                        REMOVE_PARTICIPANT_SUCCESS.getMessage()
                ));
    }
}
