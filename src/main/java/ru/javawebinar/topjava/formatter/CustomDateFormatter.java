package ru.javawebinar.topjava.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, FORMATTER);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.toString();
    }
}
