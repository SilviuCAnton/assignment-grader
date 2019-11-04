import domain.validators.AssignmentValidator;
import domain.validators.GradeValidator;
import domain.validators.StudentValidator;
import repositories.databasePersistence.AssignmentDatabaseRepository;
import repositories.databasePersistence.GradeDatabaseRepository;
import repositories.databasePersistence.StudentDatabaseRepository;
import services.service.AssignmentService;
import services.service.GradeService;
import services.service.StudentService;
import ui.Console;

public class Main {
    public static void main(String[] args) {
        //StudentService studentService = new StudentService(new StudentFileRepository(new StudentValidator(), ApplicationContext.getProperties().getProperty("data.catalog.students")));
        StudentService studentService = new StudentService(new StudentDatabaseRepository(new StudentValidator()));

        //AssignmentService assignmentService = new AssignmentService(new AssignmentFileRepository(new AssignmentValidator(), ApplicationContext.getProperties().getProperty("data.catalog.assignments")));
        AssignmentService assignmentService = new AssignmentService(new AssignmentDatabaseRepository(new AssignmentValidator()));

        //GradeService gradeService = new GradeService(studentService, assignmentService, new GradeFileRepository(new GradeValidator(), ApplicationContext.getProperties().getProperty("data.catalog.grades")));
        GradeService gradeService = new GradeService(studentService, assignmentService, new GradeDatabaseRepository(new GradeValidator()));

        Console console = new Console(studentService, assignmentService, gradeService);
        console.run();
    }
}
