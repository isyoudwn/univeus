package com.example.univeus.domain.meeting.service;

import com.example.univeus.presentation.meeting.dto.response.MainPageResponse;

public interface MainPageService {
    MainPageResponse.MainPage getMainPage(String cursor, String category, int size);

    MainPageResponse.MainPage getMainPageOffset(String category, int page, int size);
}
