package com.example.univeus.domain.meeting.model;

import com.example.univeus.domain.meeting.model.Coordinate;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class Location {
    private String address;
    @Embedded
    private Coordinate coordinate;
}
