package ru.javawebinar.topjava.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return LocalTime.parse(text, FORMATTER);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.toString();
    }
}
