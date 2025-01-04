package com.example.univeus.domain.meeting.model;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Coordinate {
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static Coordinate of(String latitude, String longitude) {
        return new Coordinate(
                new BigDecimal(latitude),
                new BigDecimal(longitude)
        );
    }
}
