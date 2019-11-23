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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import services.service.Service;
import services.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentController implements ServiceController{

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

    public StudentController() {
    }

    @FXML
    public void initialize(){
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentGroupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        studentEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentCoordinatorCol.setCellValueFactory(new PropertyValueFactory<>("coordinator"));
        studentTable.setItems(students);
    }

    @Override
    public void initialize(Service studentService) {
        initialize();
        this.studentService = (StudentService) studentService;
        List<Student> studentList = new ArrayList<>();
        ((StudentService) studentService).findAllStudents().forEach(studentList::add);
        loadTable(studentList);
    }

    private void loadTable(List studentList) {
        students = FXCollections.observableArrayList(studentList);
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
                List<Student> studentList = new ArrayList<>();
                studentService.findAllStudents().forEach(studentList::add);
                loadTable(studentList);
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
                Student st = studentService.saveStudent(student);
                if(st == null){
                    List<Student> studentList = new ArrayList<>();
                    studentService.findAllStudents().forEach(studentList::add);
                    loadTable(studentList);
                    operationResultLabel.setStyle("-fx-text-fill: green");
                    operationResultLabel.setText("The student has been saved.");
                } else {
                    operationResultLabel.setStyle("-fx-text-fill: red");
                    operationResultLabel.setText("A student already exists with given Id.");
                }

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
                List<Student> studentList = new ArrayList<>();
                studentService.findAllStudents().forEach(studentList::add);
                loadTable(studentList);
                operationResultLabel.setStyle("-fx-text-fill: green");
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
        Pulse rubberBand = new Pulse();
        rubberBand.setNode(studentTable);
        AnchorPane.setBottomAnchor(studentTable, 120.0);
        rubberBand.play();

        BounceInUp fadeIn = new BounceInUp();
        operationsPane.setVisible(true);
        fadeIn.setNode(operationsPane);
     //   fadeIn.setSpeed(0.2);
        fadeIn.play();

        searchField.setMouseTransparent(true);
        addStudentButton.setMouseTransparent(true);
        removeStudentButton.setMouseTransparent(true);
        updateStudentButton.setMouseTransparent(true);
        studentTable.setMouseTransparent(true);
    }

    private void hideOperationsPane() {
        idTextField.setText("");
        fNameTextField.setText("");
        lNameTextField.setText("");
        groupTextField.setText("");
        emailTextField.setText("");
        coordinatorTextField.setText("");
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


    public void handleSearch(KeyEvent keyEvent) {
        String text = searchField.getText().toLowerCase();
        List<Student> students = new ArrayList<>();
        studentService.findAllStudents().forEach(students::add);
        students = students.stream()
                .filter(x-> x.getFirstName().toLowerCase().contains(text) || x.getLastName().toLowerCase().contains(text))
                .collect(Collectors.toList());
        loadTable(students);
    }
}