package com.silviucanton.controllers;

import animatefx.animation.*;
import com.silviucanton.domain.auxiliary.AssignmentGradeDTO;
import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.domain.entities.Grade;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.exceptions.InvalidGradeException;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.GradeService;
import com.silviucanton.services.service.Service;
import com.silviucanton.utils.observer.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GradeController implements ServiceController, Observer<GradeService> {
    @FXML
    public CheckBox lateCheckBox;
    @FXML
    public GridPane operationsPane, updatePane;
    @FXML
    public Button addGradeButton, updateGradeButton, removeGradeButton, filtersButton;
    @FXML
    public TableView<AssignmentGradeDTO> gradeTable;
    @FXML
    public TableView<Student> studentTable;
    @FXML
    public TextField noWeeksField, gradeField, gradeTextField, professorTextField, searchField;
    @FXML
    public TableColumn<Object, Object> studentIdCol, studentFirstNameCol, studentLastNameCol, studentGroupCol, studentEmailCol, studentCoordinatorCol, assignmentCol, gradeCol, dateCol;
    @FXML
    public ComboBox<Assignment> assignmentsCombo;
    @FXML
    public CheckBox motivationCheckBox;
    @FXML
    public TextArea feedbackTextArea;
    @FXML
    public Label operationResultLabel;


    private GradeService gradeService;

    @Override
    public void update(GradeService gradeService) {
        loadStudentTable(gradeService.getStudents());
    }

    private void hideOperationsPane() {
        searchField.setMouseTransparent(false);
        addGradeButton.setMouseTransparent(false);
        removeGradeButton.setMouseTransparent(false);
        updateGradeButton.setMouseTransparent(false);
        studentTable.setMouseTransparent(false);
        gradeTable.setMouseTransparent(false);
        gradeField.setText("");
        noWeeksField.setText("");
        GridPane.setColumnSpan(studentTable, 2);
        GridPane.setColumnIndex(gradeTable, 2);
        FadeOutRight fadeOutRight = new FadeOutRight(operationsPane);
        fadeOutRight.play();
        RubberBand rubberBand = new RubberBand(studentTable.getParent());
        rubberBand.play();
    }

    private void showOperationsPane() {
        searchField.setMouseTransparent(true);
        addGradeButton.setMouseTransparent(true);
        removeGradeButton.setMouseTransparent(true);
        updateGradeButton.setMouseTransparent(true);
        studentTable.setMouseTransparent(true);
        gradeTable.setMouseTransparent(true);

        AnimationFX anim1 = new RubberBand(studentTable);
        AnimationFX anim2 = new RubberBand(gradeTable);
        GridPane.setColumnSpan(studentTable, 1);
        GridPane.setColumnIndex(gradeTable, 1);
        anim1.play();
        anim2.play();

        FadeInRight fadeInRight = new FadeInRight(operationsPane);
        operationsPane.setVisible(true);

        fadeInRight.play();
//        studentTable.setManaged(false);
//        gradeTable.setManaged(false);
    }

    @Override
    public void initialize(Service service) {
        lateCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                noWeeksField.requestFocus();
            }
        });
        studentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Student student = studentTable.getSelectionModel().getSelectedItem();
            List<AssignmentGradeDTO> assignmentGradeDTOS = gradeService.getAllGrades().stream()
                    .filter(x -> x.getStudent().equals(student))
                    .map(x -> new AssignmentGradeDTO(x.getAssignment().getDescription(), x.getValue(), x.getDate()))
                    .collect(Collectors.toList());
            loadGradesTable(assignmentGradeDTOS);
        });
        this.gradeService = (GradeService) service;
        this.gradeService.addObserver(this);
        update(this.gradeService);
    }

    private void loadStudentTable(List<Student> studentList) {
        ObservableList<Student> students = FXCollections.observableArrayList(studentList);
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        studentLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentGroupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        studentEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentCoordinatorCol.setCellValueFactory(new PropertyValueFactory<>("coordinator"));
        studentTable.setItems(students);
    }

    private void loadGradesTable(List<AssignmentGradeDTO> gradeDTOList) {
        ObservableList<AssignmentGradeDTO> gradeDTOS = FXCollections.observableArrayList(gradeDTOList);
        assignmentCol.setCellValueFactory(new PropertyValueFactory<>("assignmentName"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        gradeTable.setItems(gradeDTOS);
    }

    public void handleCancelButton() {
        hideOperationsPane();
    }

    public void handleAddButton() {
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A student must be selected in order to give a grade", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        ObservableList<Assignment> assignments = FXCollections.observableArrayList(gradeService.getAssignments());
        assignmentsCombo.setItems(assignments);
        assignmentsCombo.setConverter(new StringConverter<Assignment>() {
            @Override
            public String toString(Assignment assignment) {
                if (assignment != null) {
                    return assignment.getDescription();
                }
                return null;
            }

            @Override
            public Assignment fromString(String string) {
                return assignmentsCombo.getItems().stream().filter(x -> x.getDescription().equals(string)).findFirst().orElse(null);
            }
        });
        assignmentsCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int penalty = gradeService.getGradePenalty(newValue.getId());
                if (penalty > 0) {
                    feedbackTextArea.setText("NOTA A FOST DIMINUATA CU " + penalty + " PUNCTE DATORITA INTARZIERILOR");
                } else {
                    feedbackTextArea.setText("");
                }
            }
        });

        motivationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            int penalty = gradeService.getGradePenalty(assignmentsCombo.getSelectionModel().getSelectedItem().getId());
            int noWeeksLate = 0;
            if(!noWeeksField.getText().isEmpty() && lateCheckBox.isSelected()) {
                noWeeksLate = Integer.parseInt(noWeeksField.getText());
            }
            if(newValue) {
                if (penalty - 1 - noWeeksLate > 0) {
                    feedbackTextArea.setText("NOTA A FOST DIMINUATA CU " + (penalty-1-noWeeksLate) + " PUNCTE DATORITA INTARZIERILOR");
                } else {
                    feedbackTextArea.setText("");
                }
            } else {
                if (penalty - noWeeksLate > 0) {
                    feedbackTextArea.setText("NOTA A FOST DIMINUATA CU " + (penalty-noWeeksLate) + " PUNCTE DATORITA INTARZIERILOR");
                } else {
                    feedbackTextArea.setText("");
                }
            }
        });

        lateCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            int noWeeksLate = 0;
            int penalty = gradeService.getGradePenalty(assignmentsCombo.getSelectionModel().getSelectedItem().getId());
            int motivation = 0;
            if(motivationCheckBox.isSelected()) {
                motivation = 1;
            }
            try {
                if(newValue && !noWeeksField.getText().isEmpty()) {
                    noWeeksLate = Integer.parseInt(noWeeksField.getText());
                    if(noWeeksLate <= 0) {
                        throw new NumberFormatException();
                    }
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Number of weeks late must be a numeric value greater than 0", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            if (penalty - motivation - noWeeksLate > 0) {
                feedbackTextArea.setText("NOTA A FOST DIMINUATA CU " + (penalty-motivation-noWeeksLate) + " PUNCTE DATORITA INTARZIERILOR");
            } else {
                feedbackTextArea.setText("");
            }
        });

        noWeeksField.textProperty().addListener((observable, oldValue, newValue) -> {
            int noWeeksLate = 0;
            int penalty = gradeService.getGradePenalty(assignmentsCombo.getSelectionModel().getSelectedItem().getId());
            int motivation = 0;
            if(motivationCheckBox.isSelected()) {
                motivation = 1;
            }
            try {
                if(lateCheckBox.isSelected() && !newValue.isEmpty()) {
                    noWeeksLate = Integer.parseInt(noWeeksField.getText());
                    if(noWeeksLate <= 0) {
                        throw new NumberFormatException();
                    }
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Number of weeks late must be a numeric value greater than 0", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            if (penalty - motivation - noWeeksLate > 0) {
                feedbackTextArea.setText("NOTA A FOST DIMINUATA CU " + (penalty-motivation-noWeeksLate) + " PUNCTE DATORITA INTARZIERILOR");
            } else {
                feedbackTextArea.setText("");
            }
        });

        Assignment current = assignments.stream()
                .filter(x -> x.getDeadlineWeek() == ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate()))
                .findFirst()
                .orElse(null);
        if (current == null) {
            current = assignments.sorted(Comparator.comparingInt(Assignment::getDeadlineWeek)).get(0);
        }
        assignmentsCombo.getSelectionModel().select(current);
        showOperationsPane();
    }

    public void handleSubmitAdd() {
        int penalty = gradeService.getGradePenalty(assignmentsCombo.getSelectionModel().getSelectedItem().getId());
        String feedback = feedbackTextArea.getText();
        float value;
        int noWeeksLate = 0;
        try {
            value = Float.parseFloat(gradeField.getText());
            if(value < 1 || value > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Grade must be a numeric value between 1 and 10", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (lateCheckBox.isSelected()) {
            try {
                if(!noWeeksField.getText().isEmpty()) {
                    noWeeksLate = Integer.parseInt(noWeeksField.getText());
                    if(noWeeksLate <= 0) {
                        throw new NumberFormatException();
                    }
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Number of weeks late must be a numeric value greater than 0", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }

        int motivation = 0;
        if (motivationCheckBox.isSelected()) {
            motivation = 1;
        }
        Student student = studentTable.getSelectionModel().getSelectedItem();
        Assignment assignment = assignmentsCombo.getSelectionModel().getSelectedItem();
        float grValue = value - penalty + motivation + noWeeksLate;
        if (grValue > 10) grValue = 10;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to grade student " + student.getFirstName() + ' ' + student.getLastName() +
                "\n Grade: " + grValue + "\n" +
                "Are you sure you want to give this grade?"
                , ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            try {
                Grade res = gradeService.addGrade(student.getId(), assignment.getId(), value, student.getCoordinator(), noWeeksLate, penalty, motivationCheckBox.isSelected(), feedback);
                if (res == null) {
                    operationResultLabel.setStyle("-fx-text-fill: green");
                    studentTable.getSelectionModel().select(student);
                    operationResultLabel.setText("The grade has been added successfully");
                }
                hideOperationsPane();
            } catch (InvalidGradeException | IllegalArgumentException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                error.showAndWait();
            }
        }
    }

    public void handleRemoveGrade() {
        if (studentTable.getSelectionModel().getSelectedItem() == null || gradeTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A student and a grade must be selected in order to remove", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Assignment assignment = gradeService.getAssignments().stream()
                .filter(x -> gradeTable.getSelectionModel().getSelectedItem().getAssignmentName().equals(x.getDescription()))
                .findFirst().orElse(null);
        if (assignment != null) {
            gradeService.removeGrade(studentTable.getSelectionModel().getSelectedItem().getId(), assignment.getId());
            studentTable.getSelectionModel().selectPrevious();
            studentTable.getSelectionModel().selectNext();
            operationResultLabel.setText("The grade has been removed");
        }

    }

    public void handleUpdateGrade() {
        if (studentTable.getSelectionModel().getSelectedItem() == null || gradeTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A student and a grade must be selected in order to remove", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        gradeTextField.setText(String.valueOf(gradeTable.getSelectionModel().getSelectedItem().getGrade()));
        Assignment assignment = gradeService.getAssignments().stream()
                .filter(x -> gradeTable.getSelectionModel().getSelectedItem().getAssignmentName().equals(x.getDescription()))
                .findFirst().orElse(null);
        professorTextField.setText(String.valueOf(gradeService.findGrade(studentTable.getSelectionModel().getSelectedItem().getId(), Objects.requireNonNull(assignment).getId()).getProfessor()));
        showUpdatePanel();
    }

    public void handleSubmitUpdate() {
        float grade;
        try {
            grade = Float.parseFloat(gradeTextField.getText());
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Grade must be a numeric value", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String professor = professorTextField.getText();
        try {
            gradeService.getAssignments().stream()
                    .filter(x -> gradeTable.getSelectionModel().getSelectedItem().getAssignmentName().equals(x.getDescription()))
                    .findFirst()
                    .ifPresent(assignment -> gradeService.updateGrade(studentTable.getSelectionModel().getSelectedItem().getId(), assignment.getId(), grade, professor));
            operationResultLabel.setStyle("-fx-text-fill: green");
            operationResultLabel.setText("Grade has been updated");
            hideUpdatePanel();
        } catch (ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void handleCancelUpdateButton() {
        hideUpdatePanel();
    }

    private void showUpdatePanel() {
        RubberBand rubberBand = new RubberBand();
        rubberBand.setNode(studentTable.getParent());
        AnchorPane.setBottomAnchor(studentTable.getParent(), 160.0);
        rubberBand.play();
        RubberBand rubberBand2 = new RubberBand(filtersButton);
        AnchorPane.setBottomAnchor(filtersButton, 120.0);
        rubberBand2.play();

        FadeInUp fadeIn = new FadeInUp();
        updatePane.setVisible(true);
        fadeIn.setNode(updatePane);
        fadeIn.play();

        searchField.setMouseTransparent(true);
        addGradeButton.setMouseTransparent(true);
        removeGradeButton.setMouseTransparent(true);
        updateGradeButton.setMouseTransparent(true);
        studentTable.setMouseTransparent(true);
        gradeTable.setMouseTransparent(true);
    }

    private void hideUpdatePanel() {
        searchField.setMouseTransparent(false);
        addGradeButton.setMouseTransparent(false);
        removeGradeButton.setMouseTransparent(false);
        updateGradeButton.setMouseTransparent(false);
        studentTable.setMouseTransparent(false);
        gradeTable.setMouseTransparent(false);
        gradeTextField.setText("");
        professorTextField.setText("");

        FadeOutDown fadeOutDown = new FadeOutDown();
        fadeOutDown.setNode(updatePane);
        fadeOutDown.play();

        RubberBand slideOutUp = new RubberBand();
        slideOutUp.setNode(studentTable.getParent());
        AnchorPane.setBottomAnchor(studentTable.getParent(), 100.0);
        slideOutUp.play();
        RubberBand rubberBand = new RubberBand(filtersButton);
        AnchorPane.setBottomAnchor(filtersButton, 60.0);
        rubberBand.play();
    }

    public void handleSearch(KeyEvent keyEvent) {
        String text = searchField.getText().toLowerCase();
        List<Student> students = gradeService.getStudents();
        students = students.stream()
                .filter(x -> x.getFirstName().toLowerCase().contains(text) || x.getLastName().toLowerCase().contains(text))
                .collect(Collectors.toList());
        loadStudentTable(students);
    }
}
