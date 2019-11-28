package com.silviucanton.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.AssignmentService;
import com.silviucanton.services.service.GradeService;
import com.silviucanton.services.service.Service;
import com.silviucanton.services.service.StudentService;

import java.io.IOException;

public class HomeController implements ServiceController {

    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;

    @FXML
    private Button btnStudents, btnAssignments;


    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {

        if (mouseEvent.getSource() == btnStudents) {
            loadStage(ApplicationContext.getProperties().getProperty("model.view.students"), studentService);
        } else if(mouseEvent.getSource() == btnAssignments) {
            loadStage(ApplicationContext.getProperties().getProperty("model.view.assignments"), assignmentService);
        }

    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    private void loadStage(String fxml, Service service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxml));
            Parent root = loader.load();
            ServiceController controller =  loader.getController();
            controller.initialize(service);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            //stage.getIcons().add(new Image("/home/icons/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
