package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.service.AssignmentService;
import services.service.GradeService;
import services.service.StudentService;

import java.io.IOException;

public class HomeController {

    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;



    @FXML
    private Button btnStudents;


    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {

        if (mouseEvent.getSource() == btnStudents) {
            loadStage("/gui/students.fxml");
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

    private void loadStage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxml));
            Parent root = loader.load();
            StudentCrudController controller = loader.getController();
            controller.initialize(studentService);
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
