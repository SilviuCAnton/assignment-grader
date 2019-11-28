package com.silviucanton;

import com.silviucanton.controllers.HomeController;
import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.validators.AssignmentValidator;
import com.silviucanton.domain.validators.GradeValidator;
import com.silviucanton.domain.validators.StudentValidator;
import com.silviucanton.services.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.silviucanton.repositories.CrudRepository;
import com.silviucanton.repositories.GradeRepository;
import com.silviucanton.repositories.databasePersistence.AssignmentDatabaseRepository;
import com.silviucanton.repositories.databasePersistence.GradeDatabaseRepository;
import com.silviucanton.repositories.databasePersistence.StudentDatabaseRepository;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.AssignmentService;
import com.silviucanton.services.service.GradeService;
import com.silviucanton.services.service.StudentService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        StudentService studentService = context.getBean("studentService", StudentService.class);
        AssignmentService assignmentService = context.getBean("assignmentService", AssignmentService.class);
        GradeService gradeService = context.getBean("gradeService", GradeService.class);

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
