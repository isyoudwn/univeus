package com.example.univeus.domain.meeting;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class Location {
    private String address;
    @Embedded
    private Coordinate coordinate;
}
