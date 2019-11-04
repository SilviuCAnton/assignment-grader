package domain.auxiliary;

import domain.auxiliary.SemesterStructure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Constants;
import utils.Pair;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SemesterStructureTest {

    private static SemesterStructure sem;
    private static LocalDate startDate = LocalDate.parse("30.09.2019", Constants.DATE_TIME_FORMATTER);
    private static Pair<LocalDate, LocalDate> holiday = new Pair<>(LocalDate.parse("23.12.2019", Constants.DATE_TIME_FORMATTER), LocalDate.parse("05.01.2020", Constants.DATE_TIME_FORMATTER));

    @BeforeEach
    void setUp() {
        sem = new SemesterStructure(2, startDate, 14, holiday);
    }

    @AfterEach
    void tearDown() {
        sem = null;
    }

    @Test
    void getId() {
        assertEquals(2, sem.getId());
    }

    @Test
    void setId() {
        sem.setId(5);
        assertEquals(5, sem.getId());
    }

    @Test
    void getStartDate() {
        assertEquals(startDate, sem.getStartDate());
    }

    @Test
    void setStartDate() {
        LocalDate newStartDate = LocalDate.parse("10.10.2010", Constants.DATE_TIME_FORMATTER);
        sem.setStartDate(newStartDate);
        assertEquals(newStartDate, sem.getStartDate());
    }

    @Test
    void getNumberOfWeeks() {
        assertEquals(14, sem.getNumberOfWeeks());
    }

    @Test
    void setNumberOfWeeks() {
        sem.setNumberOfWeeks(10);
        assertEquals(10, sem.getNumberOfWeeks());
    }

    @Test
    void getHolidayWeeks() {
        assertEquals(holiday, sem.getHolidayWeeks());
    }

    @Test
    void setHolidayWeeks() {
        Pair<LocalDate, LocalDate> newHolidays = new Pair<>(LocalDate.parse("10.10.2010", Constants.DATE_TIME_FORMATTER), LocalDate.parse("20.10.2010", Constants.DATE_TIME_FORMATTER));
        sem.setHolidayWeeks(newHolidays);
        assertEquals(newHolidays, sem.getHolidayWeeks());
    }
}