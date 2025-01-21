package com.example.univeus.presentation.participant.controller;


import com.example.univeus.common.annotation.Auth;
import com.example.univeus.common.annotation.MemberOnly;
import com.example.univeus.common.response.Response;
import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

        return ResponseEntity.ok()
                .body(Response.success(
                        ResponseMessage.PARTICIPANT_SUCCESS.getCode(),
                        ResponseMessage.PARTICIPANT_SUCCESS.getMessage()
                ));
    }
}
