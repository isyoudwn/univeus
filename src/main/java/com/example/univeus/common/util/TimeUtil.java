package com.example.univeus.common.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

public class TimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static Date localDateTimeToDate(LocalDateTime localDateTime, Clock clock) {
        return Date.from(localDateTime.atZone(clock.getZone()).toInstant());
    }

    public static LocalDateTime parseToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }
}
