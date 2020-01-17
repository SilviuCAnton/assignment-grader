package com.silviucanton.controllers;

import com.silviucanton.domain.auxiliary.Role;
import com.silviucanton.domain.entities.User;
import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.StudentService;
import com.silviucanton.services.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class LoginController {

    @FXML
    public TextField userNameField;
    @FXML
    public PasswordField passwordField;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void handleLogin(ActionEvent actionEvent) {

        Optional<User> foundUser = userService.findUserByUsername(userNameField.getText());
        if(foundUser.isPresent()){
            if(passwordField.getText().equals(foundUser.get().getPassword())){
                ApplicationContext.setCurrentRole(foundUser.get().getRole());
                ApplicationContext.setCurrentUsername(userNameField.getText());
                ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gui/home.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                assert root != null;
                stage.setScene(new Scene(root));
                HomeController controller = loader.getController();
                controller.setUpAppForUserType();
                stage.getIcons().add(new Image(ApplicationContext.getProperties().getProperty("icons.grad_cap")));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Password is incorrect!", ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "A user with given username does not exist!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
