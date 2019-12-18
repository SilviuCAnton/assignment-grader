package com.silviucanton.services.config;

import com.silviucanton.domain.auxiliary.SemesterStructure;
import com.silviucanton.domain.auxiliary.YearStructure;
import com.silviucanton.utils.Constants;
import com.silviucanton.utils.observer.MyPair;

import java.time.LocalDate;
import java.util.Properties;

public class ApplicationContext {
    private static final Properties PROPERTIES = Config.getProperties();
    private static final Properties YEAR_STRUCTURE_PROPERTIES = Config.getYearStructureProperties();

    private static YearStructure yearStructure = YearStructure.getInstance(new SemesterStructure(Integer.parseInt(YEAR_STRUCTURE_PROPERTIES.getProperty("idSem1")),
                    LocalDate.parse(YEAR_STRUCTURE_PROPERTIES.getProperty("startDateSem1"), Constants.DATE_TIME_FORMATTER),
                    Integer.parseInt(YEAR_STRUCTURE_PROPERTIES.getProperty("nrWeeksSem1")),
                    new MyPair<>(LocalDate.parse(YEAR_STRUCTURE_PROPERTIES.getProperty("holidayStartSem1"), Constants.DATE_TIME_FORMATTER),
                            LocalDate.parse(YEAR_STRUCTURE_PROPERTIES.getProperty("holidayEndSem1"), Constants.DATE_TIME_FORMATTER))),
            new SemesterStructure(Integer.parseInt(YEAR_STRUCTURE_PROPERTIES.getProperty("idSem2")),
                    LocalDate.parse(YEAR_STRUCTURE_PROPERTIES.getProperty("startDateSem2"), Constants.DATE_TIME_FORMATTER),
                    Integer.parseInt(YEAR_STRUCTURE_PROPERTIES.getProperty("nrWeeksSem2")),
                    new MyPair<>(LocalDate.parse(YEAR_STRUCTURE_PROPERTIES.getProperty("holidayStartSem2"), Constants.DATE_TIME_FORMATTER),
                            LocalDate.parse(YEAR_STRUCTURE_PROPERTIES.getProperty("holidayEndSem2"), Constants.DATE_TIME_FORMATTER))));

    public static Properties getProperties() {
        return PROPERTIES;
    }

    public static YearStructure getYearStructure() {
        return yearStructure;
    }

    public static LocalDate getCurrentLocalDate() {
        String scenario = PROPERTIES.getProperty("app.run.scenario");
        if ("test".equals(scenario)) {
            return LocalDate.parse("30.10.2019", Constants.DATE_TIME_FORMATTER);
        }
        return LocalDate.now();
    }
}
