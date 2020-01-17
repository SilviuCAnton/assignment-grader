package com.silviucanton.controllers;

import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOutDown;
import animatefx.animation.RubberBand;
import com.silviucanton.domain.auxiliary.Role;
import com.silviucanton.domain.entities.Student;
import com.silviucanton.domain.entities.User;
import com.silviucanton.exceptions.ValidationException;
import com.silviucanton.services.service.Service;
import com.silviucanton.services.service.StudentService;
import com.silviucanton.services.service.UserService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserController implements ServiceController, Observer<UserService> {

    @FXML
    public Pagination pagination;
    @FXML
    private TableView<User> userTable;
    @FXML
    private GridPane operationsPane;
    @FXML
    private TextField searchField, idTextField, usernameTextField, passwordTextField, emailTextField;
    @FXML
    private Label operationResultLabel;
    @FXML
    private TableColumn<User, String> usernameCol, passwordCol, emailCol;
    @FXML
    private TableColumn<User, Integer> idCol, typeCol;
    @FXML
    private Button addUserButton, updateUserButton, removeUserButton, submitButton;
    @FXML
    private ComboBox<Role> roleCombo;

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService.removeObserver(this);
        this.userService = userService;
        userService.addObserver(this);
    }

    public UserController() {
    }

    private Node createPage(int pageIndex) {
        userTable.setItems(FXCollections.observableArrayList(userService.findAllUsersByPage(pageIndex, rowsPerPage())));
        return userTable;
    }

    int rowsPerPage() {
        return 5;
    }

    public void initialize(Service userService) {
        this.userService = (UserService) userService;
        this.userService.addObserver(this);
        roleCombo.setItems(FXCollections.observableArrayList(Arrays.asList(Role.STUDENT, Role.PROFESSOR, Role.ADMIN)));
        update(this.userService);
    }

    private void reloadTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        pagination.setPageFactory(this::createPage);
    }

    private void loadTable(List<User> userList) {
        ObservableList<User> users = FXCollections.observableArrayList(userList);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.setItems(users);
    }

    public void handleSubmitButton(ActionEvent actionEvent) {
        if (submitButton.getText().equals("Add")) {
            try {
                User user = new User(Integer.parseInt(idTextField.getText()),
                        usernameTextField.getText(),
                        passwordTextField.getText(),
                        emailTextField.getText(),
                        roleCombo.getValue());
                userService.saveUser(user);
                operationResultLabel.setText("The user has been saved.");
                hideOperationsPane();
                submitButton.setText("");
            } catch (ValidationException | IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        } else if (submitButton.getText().equals("Update")) {
            try {
                User user = new User(Integer.parseInt(idTextField.getText()),
                        usernameTextField.getText(),
                        passwordTextField.getText(),
                        emailTextField.getText(),
                        roleCombo.getValue());
                userService.updateUser(user);
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
        addUserButton.setMouseTransparent(true);
        removeUserButton.setMouseTransparent(true);
        updateUserButton.setMouseTransparent(true);
        userTable.setMouseTransparent(true);
    }

    private void hideOperationsPane() {
        searchField.setMouseTransparent(false);
        addUserButton.setMouseTransparent(false);
        removeUserButton.setMouseTransparent(false);
        updateUserButton.setMouseTransparent(false);
        userTable.setMouseTransparent(false);
        idTextField.setText("");
        usernameTextField.setText("");
        passwordTextField.setText("");
        emailTextField.setText("");
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
            List<User> users = userService.findAllUsers();
            users = users.stream()
                    .filter(x -> x.getUsername().toLowerCase().contains(text))
                    .collect(Collectors.toList());
            loadTable(users);
        }
    }

    @Override
    public void update(UserService studentService) {
        reloadTable();
    }

    public void handleAddButton(ActionEvent actionEvent) {
        submitButton.setText("Add");
        displayOperationsPane();
    }

    public void handleUpdateButton(ActionEvent actionEvent) {
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A user must be selected in order to be updated!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        User user = userTable.getSelectionModel().getSelectedItem();
        idTextField.setText(String.valueOf(user.getId()));
        usernameTextField.setText(user.getUsername());
        passwordTextField.setText(user.getPassword());
        roleCombo.getSelectionModel().select(user.getRole());
        emailTextField.setText(user.getEmail());
        submitButton.setText("Update");
        displayOperationsPane();
    }

    public void handleRemoveButton(ActionEvent actionEvent) {
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A user must be selected in order to be deleted!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try {
            User user = userTable.getSelectionModel().getSelectedItem();
            userService.deleteUser(user.getId());
            operationResultLabel.setText("The user has been removed.");
        } catch (IllegalArgumentException | ValidationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}
