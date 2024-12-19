package com.example.univeus.common.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;

public class TimeUtil {
    public static Date localDateTimeToDate(LocalDateTime localDateTime, Clock clock) {
        return Date.from(localDateTime.atZone(clock.getZone()).toInstant());
    }
}
