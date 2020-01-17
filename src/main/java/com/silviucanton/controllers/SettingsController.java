package com.silviucanton.controllers;

import com.silviucanton.services.config.ApplicationContext;
import com.silviucanton.services.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Objects;


public class SettingsController implements ServiceController {

    @FXML
    public TextField idSem1, idSem2, startDateSem1, startDateSem2, nrWeeksSem1, nrWeeksSem2, holidayStartSem1, holidayStartSem2, holidayEndSem1, holidayEndSem2;
    @FXML
    public Button saveSettingsButton;

    @Override
    public void initialize(Service service){
        idSem1.setText(ApplicationContext.getYearStructureProperties().getProperty("idSem1"));
        idSem2.setText(ApplicationContext.getYearStructureProperties().getProperty("idSem2"));
        startDateSem1.setText(ApplicationContext.getYearStructureProperties().getProperty("startDateSem1"));
        startDateSem2.setText(ApplicationContext.getYearStructureProperties().getProperty("startDateSem2"));
        nrWeeksSem1.setText(ApplicationContext.getYearStructureProperties().getProperty("nrWeeksSem1"));
        nrWeeksSem2.setText(ApplicationContext.getYearStructureProperties().getProperty("nrWeeksSem2"));
        holidayStartSem1.setText(ApplicationContext.getYearStructureProperties().getProperty("holidayStartSem1"));
        holidayStartSem2.setText(ApplicationContext.getYearStructureProperties().getProperty("holidayStartSem2"));
        holidayEndSem1.setText(ApplicationContext.getYearStructureProperties().getProperty("holidayEndSem1"));
        holidayEndSem2.setText(ApplicationContext.getYearStructureProperties().getProperty("holidayEndSem2"));
    }

    public void handleSaveSettings(ActionEvent actionEvent) {
        ApplicationContext.getYearStructureProperties().setProperty("idSem1", idSem1.getText());
        ApplicationContext.getYearStructureProperties().setProperty("idSem2", idSem2.getText());
        ApplicationContext.getYearStructureProperties().setProperty("startDateSem1", startDateSem1.getText());
        ApplicationContext.getYearStructureProperties().setProperty("startDateSem2", startDateSem2.getText());
        ApplicationContext.getYearStructureProperties().setProperty("nrWeeksSem1", nrWeeksSem1.getText());
        ApplicationContext.getYearStructureProperties().setProperty("nrWeeksSem2", nrWeeksSem2.getText());
        ApplicationContext.getYearStructureProperties().setProperty("holidayStartSem1", holidayStartSem1.getText());
        ApplicationContext.getYearStructureProperties().setProperty("holidayStartSem2", holidayStartSem2.getText());
        ApplicationContext.getYearStructureProperties().setProperty("holidayEndSem1", holidayEndSem1.getText());
        ApplicationContext.getYearStructureProperties().setProperty("holidayEndSem2", holidayEndSem2.getText());
        try( BufferedWriter out = new BufferedWriter(new FileWriter(Objects.requireNonNull(getClass().getClassLoader().getResource("yearStructure.properties")).getFile()))) {
            ApplicationContext.getYearStructureProperties().store(out, null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The changes have been saved", ButtonType.OK);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
