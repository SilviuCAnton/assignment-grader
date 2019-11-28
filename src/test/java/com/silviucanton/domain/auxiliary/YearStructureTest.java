package com.silviucanton.domain.auxiliary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.utils.Constants;
import com.silviucanton.utils.Pair;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class YearStructureTest {

    private YearStructure yearStructure;
    private SemesterStructure sem1, sem2;

    @BeforeEach
    void setUp() {
        sem1 = new SemesterStructure(1,
                LocalDate.parse("30.09.2019", Constants.DATE_TIME_FORMATTER),
                14,
                new Pair<>(LocalDate.parse("23.12.2019", Constants.DATE_TIME_FORMATTER), LocalDate.parse("05.01.2020", Constants.DATE_TIME_FORMATTER)));

        sem2 = new SemesterStructure(2,
                LocalDate.parse("24.02.2020", Constants.DATE_TIME_FORMATTER),
                14,
                new Pair<>(LocalDate.parse("20.04.2019", Constants.DATE_TIME_FORMATTER), LocalDate.parse("26.04.2020", Constants.DATE_TIME_FORMATTER)));

        yearStructure = YearStructure.getInstance(sem1, sem2);
    }

    @AfterEach
    void tearDown() {
        yearStructure = null;
    }

    @Test
    void getId() {
        assertNull(yearStructure.getId());
    }

    @Test
    void setId() {
        yearStructure.setId(1);
        assertEquals(1, yearStructure.getId());
    }

    @Test
    void getCurrentWeek() {
        assertEquals(5, yearStructure.getCurrentWeek(ApplicationContext.getCurrentLocalDate()));
    }

    @Test
    void getYear() {
        assertEquals(2019, yearStructure.getYear());
    }

    @Test
    void setYear() {
        yearStructure.setYear(2020);
        assertEquals(2020, yearStructure.getYear());
    }

    @Test
    void getSem1() {
        assertEquals(sem1, yearStructure.getSem1());
    }

    @Test
    void setSem1() {
        yearStructure.setSem1(sem2);
        assertEquals(sem2, yearStructure.getSem1());
    }

    @Test
    void getSem2() {
        assertEquals(sem2, yearStructure.getSem2());
    }

    @Test
    void setSem2() {
        yearStructure.setSem2(sem1);
        assertEquals(sem1, yearStructure.getSem2());
    }
}