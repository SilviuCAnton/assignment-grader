package com.silviucanton.controllers;

import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.RubberBand;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.services.service.Service;
import com.silviucanton.services.service.StudentService;
import com.silviucanton.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.stream.Collectors;

public class StudentController implements ServiceController, Observer<StudentService> {

    @FXML
    public Pagination pagination;
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

    public void setStudentService(StudentService studentService) {
        this.studentService.removeObserver(this);
        this.studentService = studentService;
        studentService.addObserver(this);
    }

    public StudentController() {
    }

    private Node createPage(int pageIndex) {
        studentTable.setItems(FXCollections.observableArrayList(studentService.findAllStudentsByPage(pageIndex, rowsPerPage())));
        return studentTable;
    }

    int rowsPerPage() {
        return 5;
    }

    public void initialize(Service studentService) {
        this.studentService = (StudentService) studentService;
        this.studentService.addObserver(this);
        update(this.studentService);
    }

    private void reloadTable() {
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentGroupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        studentEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentCoordinatorCol.setCellValueFactory(new PropertyValueFactory<>("coordinator"));
        pagination.setPageFactory(this::createPage);
    }

    private void loadTable(List<Student> studentList) {
        ObservableList<Student> students = FXCollections.observableArrayList(studentList);
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentGroupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        studentEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentCoordinatorCol.setCellValueFactory(new PropertyValueFactory<>("coordinator"));
        studentTable.setItems(students);
    }

    public void handleSubmitButton(ActionEvent actionEvent) {
        if (submitButton.getText().equals("Add")) {
            try {
                Student student = new Student(idTextField.getText(),
                        fNameTextField.getText(),
                        lNameTextField.getText(),
                        Integer.parseInt(groupTextField.getText()),
                        emailTextField.getText(),
                        coordinatorTextField.getText());
                studentService.saveStudent(student);
                operationResultLabel.setText("The student has been saved.");
                hideOperationsPane();
                submitButton.setText("");
            } catch (ValidationException | IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        } else if (submitButton.getText().equals("Update")) {
            try {
                Student student = new Student(idTextField.getText(),
                        fNameTextField.getText(),
                        lNameTextField.getText(),
                        Integer.parseInt(groupTextField.getText()),
                        emailTextField.getText(),
                        coordinatorTextField.getText());
                studentService.updateStudent(student);
                operationResultLabel.setText("The student has been updated.");
                hideOperationsPane();
                submitButton.setText("");
            } catch (ValidationException | IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public void handleCancelButton(ActionEvent actionEvent) {
        hideOperationsPane();
    }

    private void displayOperationsPane() {
        RubberBand slideOutUp = new RubberBand();
        slideOutUp.setNode(pagination);
        AnchorPane.setBottomAnchor(pagination, 120.0);
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
        idTextField.setText("");
        fNameTextField.setText("");
        lNameTextField.setText("");
        emailTextField.setText("");
        groupTextField.setText("");
        coordinatorTextField.setText("");
        FadeOutDown fadeOutDown = new FadeOutDown();
        fadeOutDown.setNode(operationsPane);
        fadeOutDown.play();

        RubberBand slideOutUp = new RubberBand();
        slideOutUp.setNode(pagination);
        AnchorPane.setBottomAnchor(pagination, 20.0);
        slideOutUp.play();
    }

    public void handleSearch(KeyEvent keyEvent) {
        if (searchField.getText().isEmpty()) {
            reloadTable();
        } else {
            String text = searchField.getText().toLowerCase();
            List<Student> students = studentService.findAllStudents();
            students = students.stream()
                    .filter(x -> x.getFirstName().toLowerCase().contains(text) || x.getLastName().toLowerCase().contains(text))
                    .collect(Collectors.toList());
            loadTable(students);
        }
    }

    @Override
    public void update(StudentService studentService) {
        reloadTable();
    }

    public void handleAddButton(ActionEvent actionEvent) {
        submitButton.setText("Add");
        displayOperationsPane();
    }

    public void handleUpdateButton(ActionEvent actionEvent) {
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
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
    }

    public void handleRemoveButton(ActionEvent actionEvent) {
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A student must be selected in order to be deleted!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try {
            Student student = studentTable.getSelectionModel().getSelectedItem();
            studentService.deleteStudent(student.getId());
            operationResultLabel.setText("The student has been removed.");
        } catch (IllegalArgumentException | ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}