package utils;

import domain.SemesterStructure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for application constants
 */
public class Constants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //App run scenario
    public static LocalDate configDate = LocalDate.now();

    //App test scenario
    //public static LocalDate configDate = LocalDate.parse("30.10.2019", DATE_TIME_FORMATTER);
}
