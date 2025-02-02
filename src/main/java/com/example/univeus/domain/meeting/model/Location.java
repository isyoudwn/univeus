package com.example.univeus.domain.meeting.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {
    private String address;

    @Embedded
    private Coordinate coordinate;

    public static Location of(String address, Coordinate coordinate) {
        return new Location(address, coordinate);
    }
}
