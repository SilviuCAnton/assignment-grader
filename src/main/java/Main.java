import domain.entities.Assignment;
import domain.entities.Student;
import domain.validators.AssignmentValidator;
import domain.validators.GradeValidator;
import domain.validators.StudentValidator;
import repositories.CrudRepository;
import repositories.GradeRepository;
import repositories.databasePersistence.AssignmentDatabaseRepository;
import repositories.databasePersistence.GradeDatabaseRepository;
import repositories.databasePersistence.StudentDatabaseRepository;
import repositories.filePersistence.AssignmentFileRepository;
import repositories.filePersistence.GradeFileRepository;
import repositories.filePersistence.StudentFileRepository;
import repositories.xmlPersistence.AssignmentXMLRepository;
import repositories.xmlPersistence.GradeXMLRepository;
import repositories.xmlPersistence.StudentXMLRepository;
import services.config.ApplicationContext;
import services.service.AssignmentService;
import services.service.GradeService;
import services.service.StudentService;
import ui.Console;

public class Main {
    public static void main(String[] args) {
//        Console console = new Console(studentService, assignmentService, gradeService);
//        console.run();
        App.main(args);
    }
}