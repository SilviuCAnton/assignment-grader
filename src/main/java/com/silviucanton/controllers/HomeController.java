package com.silviucanton.controllers;

import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.AssignmentService;
import com.silviucanton.services.service.GradeService;
import com.silviucanton.services.service.Service;
import com.silviucanton.services.service.StudentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HomeController implements ServiceController {

    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Autowired
    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    private void loadStage(String fxml, Service service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxml));
            Parent root = loader.load();
            ServiceController controller = loader.getController();
            controller.initialize(service);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(ApplicationContext.getProperties().getProperty("icons.grad_cap")));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleStudentsButton(ActionEvent actionEvent) {
        loadStage(ApplicationContext.getProperties().getProperty("view.students"), studentService);
    }

    public void handleAssignmentsButton(ActionEvent actionEvent) {
        loadStage(ApplicationContext.getProperties().getProperty("view.assignments"), assignmentService);
    }

    public void handleGradesButton(ActionEvent actionEvent) {
        loadStage(ApplicationContext.getProperties().getProperty("view.grades"), gradeService);
    }
}
