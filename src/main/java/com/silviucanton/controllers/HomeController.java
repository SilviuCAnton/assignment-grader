package com.silviucanton.controllers;

import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HomeController implements ServiceController {

    @FXML
    public Button dashboardButton, studentsButton, assignmentsButton, usersButton, settingsButton, gradesButton;
    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    private UserService userService;

    @Autowired
    public HomeController(StudentService studentService, AssignmentService assignmentService, GradeService gradeService, UserService userService){
        this.studentService = studentService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
        this.userService = userService;
    }

    public void setUpAppForUserType() {
        switch (ApplicationContext.getCurrentRole()){
            case STUDENT:
                studentsButton.setDisable(true);
                settingsButton.setDisable(true);
                usersButton.setDisable(true);
                assignmentsButton.setDisable(true);
                dashboardButton.setDisable(true);
                break;
            case PROFESSOR:
                usersButton.setDisable(true);
                settingsButton.setDisable(true);
                break;
            case ADMIN:
                dashboardButton.setDisable(true);
                gradesButton.setDisable(true);
                assignmentsButton.setDisable(true);
                break;
        }
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

    public void handleDashboardButton(ActionEvent actionEvent) {
        loadStage(ApplicationContext.getProperties().getProperty("view.dashboard"), gradeService);
    }

    public void handleUsersButton(ActionEvent actionEvent) {
        loadStage(ApplicationContext.getProperties().getProperty("view.users"), userService);
    }
}
