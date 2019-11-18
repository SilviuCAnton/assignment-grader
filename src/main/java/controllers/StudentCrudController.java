package controllers;

import animatefx.animation.*;
import domain.entities.Student;
import exceptions.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import services.service.StudentService;

import java.util.ArrayList;

public class StudentCrudController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private GridPane operationsPane;
    @FXML
    private TextField searchField, idTextField, fNameTextField, lNameTextField, groupTextField, emailTextField, coordinatorTextField;
    @FXML
    private Label operationResultLabel;
    @FXML
    private TableColumn<Student, String> studentIdCol, studentFirstNameCol, studentLastNameCol, studentEmailCol, studentCoordinatorCol;
    @FXML
    private TableColumn<Student, Integer> studentGroupCol;
    @FXML
    private Button addStudentButton, updateStudentButton, removeStudentButton, submitButton;

    private StudentService studentService;
    private ObservableList<Student> students;

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public StudentCrudController() {
    }

    void initialize(StudentService studentService) {
        this.studentService = studentService;
        loadTable();
    }

    private void loadTable() {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        studentService.findAllStudents().forEach(studentArrayList::add);
        students = FXCollections.observableArrayList(studentArrayList);
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentGroupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        studentEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentCoordinatorCol.setCellValueFactory(new PropertyValueFactory<>("coordinator"));
        studentTable.setItems(students);
    }

    public void handleButtonsClick(ActionEvent actionEvent) {
        if(actionEvent.getSource() == addStudentButton) {
            submitButton.setText("Add");
            displayOperationsPane();
        } else if(actionEvent.getSource() == updateStudentButton) {
            if(studentTable.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "A student must be selected in order to be updated!", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            Student student = studentTable.getSelectionModel().getSelectedItem();
            idTextField.setText(student.getId());
            fNameTextField.setText(student.getFirstName());
            lNameTextField.setText(student.getLastName());
            groupTextField.setText(String.valueOf(student.getGroup()));
            emailTextField.setText(student.getEmail());
            coordinatorTextField.setText(student.getCoordinator());
            submitButton.setText("Update");
            displayOperationsPane();
        } else if(actionEvent.getSource() == removeStudentButton) {
            if(studentTable.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "A student must be selected in order to be deleted!", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            try{
                Student student = studentTable.getSelectionModel().getSelectedItem();
                studentService.deleteStudent(student.getId());
                loadTable();
                operationResultLabel.setText("The student has been removed.");
            } catch(IllegalArgumentException | ValidationException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public void handleSubmitButton(ActionEvent actionEvent) {
        if(submitButton.getText().equals("Add")) {
            try{
                Student student = new Student(idTextField.getText(),
                        fNameTextField.getText(),
                        lNameTextField.getText(),
                        Integer.parseInt(groupTextField.getText()),
                        emailTextField.getText(),
                        coordinatorTextField.getText());
                studentService.saveStudent(student);
                loadTable();
                operationResultLabel.setText("The student has been saved.");
            } catch (ValidationException | IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            } finally {
                hideOperationsPane();
                submitButton.setText("");
            }
        } else if (submitButton.getText().equals("Update")) {
            try{
                Student student = new Student(idTextField.getText(),
                        fNameTextField.getText(),
                        lNameTextField.getText(),
                        Integer.parseInt(groupTextField.getText()),
                        emailTextField.getText(),
                        coordinatorTextField.getText());
                studentService.updateStudent(student);
                loadTable();
                operationResultLabel.setText("The student has been updated.");
            } catch (ValidationException | IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            } finally {
                hideOperationsPane();
                submitButton.setText("");
            }
        }
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        hideOperationsPane();
    }

    private void displayOperationsPane() {
        RubberBand slideOutUp = new RubberBand();
        slideOutUp.setNode(studentTable);
        AnchorPane.setBottomAnchor(studentTable, 120.0);
        slideOutUp.play();

        FadeInUp fadeIn = new FadeInUp();
        operationsPane.setVisible(true);
        fadeIn.setNode(operationsPane);
        fadeIn.play();

        searchField.setMouseTransparent(true);
        addStudentButton.setMouseTransparent(true);
        removeStudentButton.setMouseTransparent(true);
        updateStudentButton.setMouseTransparent(true);
        studentTable.setMouseTransparent(true);
    }

    private void hideOperationsPane() {
        searchField.setMouseTransparent(false);
        addStudentButton.setMouseTransparent(false);
        removeStudentButton.setMouseTransparent(false);
        updateStudentButton.setMouseTransparent(false);
        studentTable.setMouseTransparent(false);
        FadeOutDown fadeOutDown = new FadeOutDown();
        fadeOutDown.setNode(operationsPane);
        fadeOutDown.play();

        RubberBand slideOutUp = new RubberBand();
        slideOutUp.setNode(studentTable);
        AnchorPane.setBottomAnchor(studentTable, 20.0);
        slideOutUp.play();
    }


}
