package com.example.univeus.domain.meeting;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public class PostDeadLine {
    private LocalDate date;
    private LocalTime time;
}
