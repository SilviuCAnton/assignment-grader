package repositories.xmlPersistence;

import domain.entities.Assignment;
import domain.entities.Grade;
import domain.entities.Student;
import domain.validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import repositories.CrudRepository;
import repositories.GradeRepository;
import utils.Constants;
import utils.Pair;

import java.time.LocalDate;

/**
 * Grade repository - XML file data persistence
 */
public class GradeXMLRepository extends AbstractXMLRepository<Pair<String, Integer>, Grade> implements GradeRepository {
    private CrudRepository<String, Student> studentRepo;
    private CrudRepository<Integer, Assignment> assignmentRepo;

    public GradeXMLRepository(Validator<Grade> validator, String fileName, CrudRepository<String, Student> studentRepo, CrudRepository<Integer, Assignment> assignmentRepo) {
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
    public CrudRepository<String, Student> getStudentRepo() {
        return studentRepo;
    }

    @Override
    public CrudRepository<Integer, Assignment> getAssignmentRepo() {
        return assignmentRepo;
    }
}
