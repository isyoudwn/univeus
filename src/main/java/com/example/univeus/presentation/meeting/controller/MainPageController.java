package com.example.univeus.presentation.meeting.controller;

import com.example.univeus.common.response.Response;
import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.meeting.service.MainPageService;
import com.example.univeus.presentation.meeting.dto.request.MainPageRequest;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse.MainPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/main")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("")
    public ResponseEntity<Response<MainPage>> getMainPages(
            MainPageRequest.MainPageCursor mainPageCursor
    ) {
        MainPageResponse.MainPage mainPage = mainPageService.getMainPage(
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
        MainPageResponse.MainPage mainPage = mainPageService.getMainPageOffset(
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
}
