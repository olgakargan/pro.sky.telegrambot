package pro.sky.telegrambot.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public final class ParsingUtil {
    private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    public static final String patternDateTime = "dd.MM.yyyy HH:mm";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternDateTime);

    // 01.01.2022 20:00
    public static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, formatterDate);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, formatterTime);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String formatTime(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }

    public static Long parseNumber(String number) {
        if (!number.matches("[1-9][\\d]*")) {
            return null;
        }
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}