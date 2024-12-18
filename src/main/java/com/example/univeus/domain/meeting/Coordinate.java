package com.example.univeus.domain.meeting;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Coordinate {
    private BigDecimal latitude;
    private BigDecimal hardness;
}
