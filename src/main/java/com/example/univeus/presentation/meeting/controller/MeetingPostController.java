package com.example.univeus.presentation.meeting.controller;

import com.example.univeus.common.annotation.Auth;
import com.example.univeus.common.annotation.MemberOnly;
import com.example.univeus.common.response.Response;
import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.meeting.service.MeetingPostService;
import com.example.univeus.presentation.meeting.dto.request.MainPageRequest;
import com.example.univeus.presentation.meeting.dto.request.MeetingPostRequest;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            @Valid @RequestBody MeetingPostRequest.Write writeRequest
    ) {
        Long memberId = accessor.getMemberId();
        meetingPostService.writePost(memberId, writeRequest.meetingPostContent(),
                writeRequest.meetingPostUris());

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.MEETING_UPLOAD_SUCCESS.getCode(),
                        ResponseMessage.MEETING_UPLOAD_SUCCESS.getMessage()
                ));
    }

    @MemberOnly
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Response<String>> deleteMeetingPost(
            @Auth Accessor accessor,
            @PathVariable String postId
    ) {
        Long memberId = accessor.getMemberId();
        meetingPostService.deletePost(memberId, Long.valueOf(postId));

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.DELETE_MEETING_SUCCESS.getCode(),
                        ResponseMessage.DELETE_MEETING_SUCCESS.getMessage()
                ));
    }

    @MemberOnly
    @PatchMapping("/post/{postId}")
    public ResponseEntity<Response<String>> updateMeetingPost(
            @Auth Accessor accessor,
            @PathVariable String postId,
            @Valid @RequestBody MeetingPostRequest.Update updateRequest
    ) {
        Long memberId = accessor.getMemberId();
        meetingPostService.updatePost(memberId, Long.valueOf(postId), updateRequest);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.UPDATE_MEETING_SUCCESS.getCode(),
                        ResponseMessage.UPDATE_MEETING_SUCCESS.getMessage()
                ));
    }

    @GetMapping("/posts")
    public ResponseEntity<Response<MainPageResponse.MainPage>> getMainPages(
            MainPageRequest.MainPageCursor mainPageCursor
    ) {
        MainPageResponse.MainPage mainPage = meetingPostService.getMeetingPosts(
                mainPageCursor.id(),
                mainPageCursor.category(),
                Integer.parseInt(mainPageCursor.size()));

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.MAIN_PAGE_RENDERING_SUCCESS.getCode(),
                        ResponseMessage.MAIN_PAGE_RENDERING_SUCCESS.getMessage(),
                        mainPage
                ));
    }

    @GetMapping("/offsets")
    public ResponseEntity<Response<MainPageResponse.MainPage>> getMainPagesByOffset(
            MainPageRequest.MainPageOffset mainPageOffset
    ) {
        MainPageResponse.MainPage mainPage = meetingPostService.getMeetingPostsOffset(
                mainPageOffset.category(),
                Integer.parseInt(mainPageOffset.page()),
                Integer.parseInt(mainPageOffset.size()));

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.MAIN_PAGE_RENDERING_SUCCESS.getCode(),
                        ResponseMessage.MAIN_PAGE_RENDERING_SUCCESS.getMessage(),
                        mainPage
                ));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Response<MeetingPostResponse.MeetingPost>> getPost(
            @Auth Accessor accessor,
            @PathVariable String postId
    ) {
        Long memberId = accessor.getMemberId();
        MeetingPostResponse.MeetingPost meetingPost = meetingPostService.readPost(memberId, Long.valueOf(postId));

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.READ_MEETING_SUCCESS.getCode(),
                        ResponseMessage.READ_MEETING_SUCCESS.getCode(),
                        meetingPost
                ));
    }
}
