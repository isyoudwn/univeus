package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse;

public interface MainPageService {
    MainPageResponse.MainPage getMainPage(Long cursor, MeetingCategory meetingCategory);

    MainPageResponse.MainPage getMainPageOffset(String category, int page);
}
