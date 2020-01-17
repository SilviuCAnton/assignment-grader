package com.silviucanton.controllers;

import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.RubberBand;
import com.silviucanton.domain.entities.Assignment;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.AssignmentService;
import com.silviucanton.services.service.Service;
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

public class AssignmentController implements ServiceController, Observer<AssignmentService> {
    @FXML
    public TableColumn<Object, Object> assignmentIdCol;
    @FXML
    public TableColumn<Object, Object> assignmentDescriptionCol;
    @FXML
    public TableColumn<Object, Object> assignmentDeadlineWeekCol;
    @FXML
    public TableColumn<Object, Object> assignmentStartWeekCol;
    @FXML
    public GridPane operationsPane;
    @FXML
    public TextField idTextField, descriptionTextField, startWeekTextField, deadlineWeekTextField, searchField;
    @FXML
    public Button submitButton, addAssignmentButton, removeAssignmentButton, updateAssignmentButton;
    @FXML
    public Label operationResultLabel;
    @FXML
    public TableView<Assignment> assignmentTable;
    @FXML
    public Pagination pagination;

    private AssignmentService assignmentService;

    public void handleSubmitButton(ActionEvent actionEvent) {
        if (submitButton.getText().equals("Add")) {
            try {
                Assignment assignment = new Assignment(Integer.parseInt(idTextField.getText()),
                        descriptionTextField.getText(),
                        Integer.parseInt(deadlineWeekTextField.getText()));
                assignmentService.addAssignment(assignment);
                operationResultLabel.setText("The student has been saved.");
                startWeekTextField.setEditable(true);
                hideOperationsPane();
                submitButton.setText("");
            } catch (ValidationException | IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        } else if (submitButton.getText().equals("Update")) {
            try {
                Assignment assignment = new Assignment(Integer.parseInt(idTextField.getText()),
                        descriptionTextField.getText(),
                        Integer.parseInt(deadlineWeekTextField.getText()));
                assignment.setStartWeek(Integer.parseInt(startWeekTextField.getText()));
                assignmentService.updateAssignment(assignment);
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

    public void handleAddButton(ActionEvent actionEvent) {
        submitButton.setText("Add");
        startWeekTextField.setEditable(false);
        startWeekTextField.setText(String.valueOf(ApplicationContext.getYearStructure().getCurrentWeek(ApplicationContext.getCurrentLocalDate())));
        displayOperationsPane();
    }

    public void handleUpdateButton(ActionEvent actionEvent) {
        if (assignmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An assignment must be selected in order to be updated!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Assignment assignment = assignmentTable.getSelectionModel().getSelectedItem();
        idTextField.setText(String.valueOf(assignment.getId()));
        descriptionTextField.setText(assignment.getDescription());
        startWeekTextField.setText(String.valueOf(assignment.getStartWeek()));
        deadlineWeekTextField.setText(String.valueOf(assignment.getDeadlineWeek()));
        submitButton.setText("Update");
        displayOperationsPane();
    }

    public void handleRemoveButton(ActionEvent actionEvent) {
        if (assignmentTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An assignment must be selected in order to be deleted!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try {
            Assignment assignment = assignmentTable.getSelectionModel().getSelectedItem();
            assignmentService.deleteAssignment(assignment.getId());
            operationResultLabel.setText("The assignment has been removed.");
        } catch (IllegalArgumentException | ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private Node createPage(int pageIndex) {
        assignmentTable.setItems(FXCollections.observableArrayList(assignmentService.findAllAssignmentsByPage(pageIndex, rowsPerPage())));
        return assignmentTable;
    }

    int rowsPerPage() {
        return 10;
    }

    private void reloadTable() {
        assignmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assignmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        assignmentStartWeekCol.setCellValueFactory(new PropertyValueFactory<>("startWeek"));
        assignmentDeadlineWeekCol.setCellValueFactory(new PropertyValueFactory<>("deadlineWeek"));
        pagination.setPageFactory(this::createPage);
    }

    private void loadTable(List<Assignment> assignmentList) {
        ObservableList<Assignment> assignments = FXCollections.observableArrayList(assignmentList);
        assignmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assignmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        assignmentStartWeekCol.setCellValueFactory(new PropertyValueFactory<>("startWeek"));
        assignmentDeadlineWeekCol.setCellValueFactory(new PropertyValueFactory<>("deadlineWeek"));
        assignmentTable.setItems(assignments);
    }

    @Override
    public void update(AssignmentService assignmentService) {
        reloadTable();
    }

    @Override
    public void initialize(Service service) {
        this.assignmentService = (AssignmentService) service;
        this.assignmentService.addObserver(this);
        update(this.assignmentService);
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
        addAssignmentButton.setMouseTransparent(true);
        removeAssignmentButton.setMouseTransparent(true);
        updateAssignmentButton.setMouseTransparent(true);
        assignmentTable.setMouseTransparent(true);
    }

    private void hideOperationsPane() {
        searchField.setMouseTransparent(false);
        addAssignmentButton.setMouseTransparent(false);
        removeAssignmentButton.setMouseTransparent(false);
        updateAssignmentButton.setMouseTransparent(false);
        assignmentTable.setMouseTransparent(false);
        idTextField.setText("");
        descriptionTextField.setText("");
        startWeekTextField.setText("");
        deadlineWeekTextField.setText("");
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
            List<Assignment> assignments = assignmentService.findAllAssignments();
            assignments = assignments.stream()
                    .filter(x -> x.getDescription().toLowerCase().contains(text))
                    .collect(Collectors.toList());
            loadTable(assignments);
        }

    }
}
