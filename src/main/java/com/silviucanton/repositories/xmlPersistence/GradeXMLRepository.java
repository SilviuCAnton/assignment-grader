package com.silviucanton.repositories.xmlPersistence;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.GradeId;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.Validator;
import com.silviucanton.repositories.CrudNoSpringRepo;
import com.silviucanton.repositories.GradeRepository;
import com.silviucanton.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.time.LocalDate;

/**
 * Grade repository - XML file data persistence
 */
@Component
public class GradeXMLRepository extends AbstractXMLRepository<Grade, GradeId> implements GradeRepository {
    private CrudNoSpringRepo<Student, String> studentRepo;
    private CrudNoSpringRepo<Assignment, Integer> assignmentRepo;

    @Autowired
    public GradeXMLRepository(Validator<Grade> validator, @Value("${data.catalog.xml.grades}") String fileName, @Qualifier("studentXMLRepository") CrudNoSpringRepo<Student, String> studentRepo, @Qualifier("assignmentXMLRepository") CrudNoSpringRepo<Assignment, Integer> assignmentRepo) {
        super(validator, fileName, "grade", false);
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
        loadFromXMLFile();
    }

    /**
     * parses a grade from the XML file
     *
     * @param node - the node to be parsed
     * @return grade - Grade
     */
    @Override
    Grade readEntity(Node node) {
        Element element = (Element) node;
        String studentId = element.getAttribute("studentId");
        Student student = studentRepo.findOne(studentId);
        int assignmentId = Integer.parseInt(element.getAttribute("assignmentId"));
        Assignment assignment = assignmentRepo.findOne(assignmentId);
        LocalDate date = LocalDate.parse(element.getElementsByTagName("date").item(0).getTextContent(), Constants.DATE_TIME_FORMATTER);
        float value = Float.parseFloat(element.getElementsByTagName("value").item(0).getTextContent());
        String professor = element.getElementsByTagName("professor").item(0).getTextContent();
        Grade grade = new Grade(student, assignment, value, professor);
        grade.setDate(date);
        return grade;
    }

    /**
     * creates XML node from a grade
     *
     * @param document - the XML document object
     * @param grade    - Grade
     * @return gradeXML - the XML node
     */
    @Override
    Element createElementFromEntity(Document document, Grade grade) {
        LocalDate date = grade.getDate();
        String month = "";
        String day = "";
        if (date.getMonthValue() < 10) {
            month += '0';
        }
        if (date.getDayOfMonth() < 10) {
            day += '0';
        }
        month += date.getMonthValue();
        day += date.getDayOfMonth();

        Element gradeXML = document.createElement("grade");
        gradeXML.setAttribute("studentId", grade.getId().getFirst());
        gradeXML.setAttribute("assignmentId", String.valueOf(grade.getId().getSecond()));
        gradeXML.appendChild(createNodeElement(document, gradeXML, "date", day + '.' + month + '.' + date.getYear()));
        gradeXML.appendChild(createNodeElement(document, gradeXML, "value", String.valueOf(grade.getValue())));
        gradeXML.appendChild(createNodeElement(document, gradeXML, "professor", grade.getProfessor()));

        return gradeXML;
    }

    @Override
    public CrudNoSpringRepo<Student, String> getStudentRepo() {
        return studentRepo;
    }

    @Override
    public CrudNoSpringRepo<Assignment, Integer> getAssignmentRepo() {
        return assignmentRepo;
    }
}
