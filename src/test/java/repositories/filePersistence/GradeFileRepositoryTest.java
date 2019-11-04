package repositories.filePersistence;

import domain.entities.Grade;
import domain.validators.GradeValidator;
import exceptions.InvalidGradeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.config.ApplicationContext;
import utils.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GradeFileRepositoryTest {

    private GradeFileRepository gradeFileRepository;
    private Path path;
    private Grade gr1, gr2;

    @BeforeEach
    void setUp() {
        gradeFileRepository = new GradeFileRepository(new GradeValidator(), ApplicationContext.getProperties().getProperty("data.test.catalog.grades"));
        path = Paths.get(ApplicationContext.getProperties().getProperty("data.test.catalog.grades"));
        gr1 = new Grade("abcd1234", 1, 8.6f, "Prof1");
        gr2 = new Grade("abcd1235", 2, 7.3f, "Prof2");
        gradeFileRepository.save(gr1);
    }

    @AfterEach
    void tearDown() {
        try {
            BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void save() {
        ArrayList<Grade> grades = new ArrayList<>();
        gradeFileRepository.findAll().forEach(grades::add);
        assertEquals(1, grades.size());
        gradeFileRepository.save(gr2);
        grades = new ArrayList<>();
        gradeFileRepository.findAll().forEach(grades::add);
        assertEquals(2, grades.size());
    }

    @Test
    void saveBadId() {
        Grade badGrade = new Grade("", 1, 6.5f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadValue1() {
        Grade badGrade = new Grade("abcd1234", 1, -5.3f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadValue2() {
        Grade badGrade = new Grade("abcd1234", 1, 15.3f, "ProfTest");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadDate() {
        Grade badGrade = new Grade("abcd1234", 1, 6.5f, "ProfTest");
        badGrade.setDate(null);
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveBadProfessor() {
        Grade badGrade = new Grade("abcd1234", 1, 6.5f, "");
        assertThrows(InvalidGradeException.class, () -> gradeFileRepository.save(badGrade));
    }

    @Test
    void saveNullGrade() {
        assertThrows(IllegalArgumentException.class, () -> gradeFileRepository.save(null));
    }

    @Test
    void delete() {
        assertEquals(gr1, gradeFileRepository.delete(new Pair<>("abcd1234", 1)));
        assertNull(gradeFileRepository.delete(new Pair<>("abcd1238", 3)));
    }

    @Test
    void update() {
        gradeFileRepository.update(new Grade("abcd1234",1,6.5f, "newProf"));
        assertEquals(new Grade("abcd1234",1,6.5f, "newProf"), gradeFileRepository.findOne(new Pair<>("abcd1234", 1)));
        assertNull(gradeFileRepository.update(new Grade("abcd1234",4,6.5f, "newProf")));
    }

    @Test
    void findOne() {
        gradeFileRepository.save(gr2);
        assertEquals(gr1, gradeFileRepository.findOne(new Pair<>("abcd1234", 1)));
        assertEquals(gr2, gradeFileRepository.findOne(new Pair<>("abcd1235", 2)));
        assertNull(gradeFileRepository.findOne(new Pair<>("abcd1235", 1)));
    }

    @Test
    void findAll() {
        gradeFileRepository.save(gr2);
        ArrayList<Grade> grades = new ArrayList<>();
        gradeFileRepository.findAll().forEach(grades::add);
        assertEquals(2, grades.size());
    }

    @Test
    void parseEntity() {
        assertEquals(gr1, gradeFileRepository.parseEntity("abcd1234/1/30.10.2019/8.6/Prof1"));
    }
}