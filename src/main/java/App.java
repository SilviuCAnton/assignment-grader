import controllers.HomeController;
import domain.entities.Assignment;
import domain.entities.Student;
import domain.validators.AssignmentValidator;
import domain.validators.GradeValidator;
import domain.validators.StudentValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repositories.CrudRepository;
import repositories.GradeRepository;
import repositories.databasePersistence.AssignmentDatabaseRepository;
import repositories.databasePersistence.GradeDatabaseRepository;
import repositories.databasePersistence.StudentDatabaseRepository;
import repositories.xmlPersistence.AssignmentXMLRepository;
import repositories.xmlPersistence.GradeXMLRepository;
import repositories.xmlPersistence.StudentXMLRepository;
import services.config.ApplicationContext;
import services.service.AssignmentService;
import services.service.GradeService;
import services.service.StudentService;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        String dbConnectionString, dbUserName, dbPassword;

        dbConnectionString = ApplicationContext.getProperties().getProperty("data.db.connectionString");
        dbUserName = ApplicationContext.getProperties().getProperty("data.db.userName");
        dbPassword = ApplicationContext.getProperties().getProperty("data.db.password");

//        CrudRepository<String, Student> studentRepo = new StudentXMLRepository(new StudentValidator(), ApplicationContext.getProperties().getProperty("data.catalog.xml.students"));
//        CrudRepository<String, Student> studentRepo = new StudentFileRepository(new StudentValidator(), ApplicationContext.getProperties().getProperty("data.catalog.students"));
        CrudRepository<String, Student> studentRepo = new StudentDatabaseRepository(new StudentValidator(), dbConnectionString, dbUserName, dbPassword);

//        CrudRepository<Integer, Assignment> assignmentRepo = new AssignmentXMLRepository(new AssignmentValidator(), ApplicationContext.getProperties().getProperty("data.catalog.xml.assignments"));
//        CrudRepository<Integer, Assignment> assignmentRepo = new AssignmentFileRepository(new AssignmentValidator(), ApplicationContext.getProperties().getProperty("data.catalog.assignments"));
        CrudRepository<Integer, Assignment> assignmentRepo = new AssignmentDatabaseRepository(new AssignmentValidator(), dbConnectionString, dbUserName, dbPassword);

//        GradeRepository gradeRepo = new GradeXMLRepository(new GradeValidator(), ApplicationContext.getProperties().getProperty("data.catalog.xml.grades"), studentRepo, assignmentRepo);
//        GradeRepository gradeRepo = new GradeFileRepository(new GradeValidator(), ApplicationContext.getProperties().getProperty("data.catalog.grades"), studentRepo, assignmentRepo);
        GradeRepository gradeRepo = new GradeDatabaseRepository(new GradeValidator(), dbConnectionString, dbUserName, dbPassword, studentRepo, assignmentRepo);

        StudentService studentService = new StudentService(studentRepo);
        AssignmentService assignmentService = new AssignmentService(assignmentRepo);
        GradeService gradeService = new GradeService(gradeRepo);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ApplicationContext.getProperties().getProperty("model.view.home")));
        Parent root = loader.load();
        HomeController homeController = loader.getController();
        homeController.setStudentService(studentService);
        homeController.setAssignmentService(assignmentService);
        homeController.setGradeService(gradeService);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
