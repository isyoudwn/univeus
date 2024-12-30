package com.example.univeus.domain.meeting.model;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public class MeetingDateTime {
    private LocalDate meetingDate;
    private LocalTime meetingTime;
}
