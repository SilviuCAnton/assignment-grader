package com.silviucanton.controllers;

import com.silviucanton.domain.auxiliary.AssignmentGradeDTO;
import com.silviucanton.domain.auxiliary.StudentGradeDTO;
import com.silviucanton.services.service.GradeService;
import com.silviucanton.services.service.Service;
import com.silviucanton.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardController implements ServiceController, Observer<GradeService> {

    @FXML
    public TableView<StudentGradeDTO> studentTable;
    @FXML
    public PieChart passedStudentsPieChart, neverLatePieChart;
    @FXML
    public BarChart averageGradeBarChart;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public TableColumn<Object, Object> studentNameCol, studentGradeCol;
    @FXML
    public Label hardestLabel;

    private GradeService gradeService;
    private List<Series> allSeries = new ArrayList<>();

    @Override
    public void update(GradeService gradeService) {
        createGradesBarChart();
        passedStudentsPieChart.setData(createPassedChart());
        neverLatePieChart.setData(createNeverLateChart());
        AssignmentGradeDTO hardestAssignment = gradeService.getHardestAssignment();
        hardestLabel.setText(hardestAssignment.getAssignmentName() + " (Average of " + hardestAssignment.getGrade() + ")");
    }

    @Override
    public void initialize(Service service) {
        this.gradeService = (GradeService) service;
        this.gradeService.addObserver(this);
        update(this.gradeService);
        createGradesBarChart();
        passedStudentsPieChart.setData(createPassedChart());
        neverLatePieChart.setData(createNeverLateChart());
        AssignmentGradeDTO hardestAssignment = gradeService.getHardestAssignment();
        hardestLabel.setText(hardestAssignment.getAssignmentName() + " (Average of " + hardestAssignment.getGrade() + ")");
    }

    private ObservableList<PieChart.Data> createPassedChart() {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        long passed = gradeService.getPassedStudents().size();
        long failed = gradeService.getFinalGrades().size() - passed;
        list.addAll(new PieChart.Data("Passed", passed),
                new PieChart.Data("Failed", failed)
        );
        return list;
    }

    private ObservableList<PieChart.Data> createNeverLateChart() {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        long neverLate = gradeService.getNeverLateStudents().size();
        long late = gradeService.getFinalGrades().size() - neverLate;
        list.addAll(new PieChart.Data("Never late", neverLate),
                new PieChart.Data("Late at least once", late)
        );
        return list;
    }

    private void createGradesBarChart() {
        averageGradeBarChart.getData().removeAll(allSeries);
        allSeries.clear();
        Map<Integer, Integer> finalGradesStats = gradeService.getFinalGradesStatistics();
        int index = 0;
        XYChart.Series series;
        for (Map.Entry<Integer, Integer> x : finalGradesStats.entrySet()) {
            series = new XYChart.Series();
            series.setName(x.getKey().toString() + '-' + (x.getKey() + 1));
            series.getData().add(new XYChart.Data("Final grades", x.getValue()));
            allSeries.add(series);
            averageGradeBarChart.getData().add(index++, series);
        }
    }

    private void loadStudentTable(List<StudentGradeDTO> studentList) {
        ObservableList<StudentGradeDTO> students = FXCollections.observableArrayList(studentList);
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentGradeCol.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
        studentTable.setItems(students);
    }

    public void handleShowFinalGrades(ActionEvent actionEvent) {
        loadStudentTable(gradeService.getFinalGrades());
    }

    public void handleShowPassed(ActionEvent actionEvent) {
        loadStudentTable(gradeService.getPassedStudents());
    }

    public void handleShowNeverLate(ActionEvent actionEvent) {
        loadStudentTable(gradeService.getNeverLateStudents());
    }
}
