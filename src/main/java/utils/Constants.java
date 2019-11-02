package utils;

import domain.SemesterStructure;
import domain.YearStructure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for application constants
 */
public class Constants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static YearStructure yearStructure = YearStructure.getInstance(new SemesterStructure(1,
                    LocalDate.parse("30.09.2019", Constants.DATE_TIME_FORMATTER),
                    14,
                    new Pair<>(LocalDate.parse("23.12.2019", Constants.DATE_TIME_FORMATTER), LocalDate.parse("05.01.2020", Constants.DATE_TIME_FORMATTER))),
            new SemesterStructure(2,
                    LocalDate.parse("24.02.2020", Constants.DATE_TIME_FORMATTER),
                    14,
                    new Pair<>(LocalDate.parse("20.04.2019", Constants.DATE_TIME_FORMATTER), LocalDate.parse("26.04.2020", Constants.DATE_TIME_FORMATTER))));


    //App run scenario
    //public static LocalDate configDate = LocalDate.now();

    //App test scenario
    public static LocalDate configDate = LocalDate.parse("30.10.2019", DATE_TIME_FORMATTER);
}
