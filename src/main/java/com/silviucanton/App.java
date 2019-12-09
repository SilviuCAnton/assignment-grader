package com.silviucanton;

import com.silviucanton.services.config.ApplicationContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ApplicationContext.getProperties().getProperty("view.home")));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.getIcons().add(new Image(ApplicationContext.getProperties().getProperty("icons.grad_cap")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
