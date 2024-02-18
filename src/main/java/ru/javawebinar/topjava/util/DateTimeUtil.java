package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        boolean comparisonWithStartTime = startTime == null || lt.compareTo(startTime) >= 0;
        boolean comparisonWithEndTime = endTime == null || lt.compareTo(endTime) < 0;
        return comparisonWithStartTime && comparisonWithEndTime;
    }

    public static boolean isBetweenDate(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX : endDate;
        return !(ld.isBefore(startDate) || ld.isAfter(endDate));
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

