package com.silviucanton.controllers;

import com.silviucanton.domain.auxiliary.AssignmentGradeDTO;
import com.silviucanton.domain.auxiliary.StudentGradeDTO;
import com.silviucanton.services.service.GradeService;
import com.silviucanton.services.service.Service;
import com.silviucanton.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    public Label hardestLabel, percentLabel;

    private GradeService gradeService;
    private List<Series> allSeries = new ArrayList<>();

    @Override
    public void update(GradeService gradeService) {
        List<StudentGradeDTO> finalGrades = gradeService.getFinalGrades();
        List<StudentGradeDTO> neverLate = gradeService.getNeverLateStudents(finalGrades);
        List<StudentGradeDTO> passedStudents = gradeService.getPassedStudents();
        createGradesBarChart();
        passedStudentsPieChart.setData(createPassedChart(passedStudents, finalGrades));
        percentLabel.toFront();
        for (final PieChart.Data data : passedStudentsPieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            percentLabel.setTranslateX(e.getSceneX());
                            percentLabel.setTranslateY(e.getSceneY() - 300);
                            String str = " student";
                            if (data.getPieValue() > 1)
                                str += 's';
                            percentLabel.setText((int) data.getPieValue() + str);
                        }
                    });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            percentLabel.setText("");
                        }
                    });
        }

        neverLatePieChart.setData(createNeverLateChart(neverLate, finalGrades));
        for (final PieChart.Data data : neverLatePieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                    e -> {
                        percentLabel.setTranslateX(e.getSceneX());
                        percentLabel.setTranslateY(e.getSceneY() - 300);
                        String str = " student";
                        if (data.getPieValue() > 1)
                            str += 's';
                        percentLabel.setText((int) data.getPieValue() + str);
                    });
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> percentLabel.setText(""));
        }
        AssignmentGradeDTO hardestAssignment = gradeService.getHardestAssignment();
        hardestLabel.setText(hardestAssignment.getAssignmentName() + " (Average of " + hardestAssignment.getGrade() + ")");
    }

    @Override
    public void initialize(Service service) {
        this.gradeService = (GradeService) service;
        this.gradeService.addObserver(this);
        update(gradeService);
    }

    private ObservableList<PieChart.Data> createPassedChart(List<StudentGradeDTO> passedStd, List<StudentGradeDTO> finalGrades) {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        long passed = passedStd.size();
        long failed = finalGrades.size() - passed;
        list.addAll(new PieChart.Data("Passed", passed),
                new PieChart.Data("Failed", failed)
        );
        return list;
    }

    private ObservableList<PieChart.Data> createNeverLateChart(List<StudentGradeDTO> neverLateStd, List<StudentGradeDTO> finalGrades) {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        long neverLate = neverLateStd.size();
        long late = finalGrades.size() - neverLate;
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
        loadStudentTable(gradeService.getNeverLateStudents(gradeService.getFinalGrades()));
    }

    public void handleExportFinalGrades(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            gradeService.exportPdfFinalGrades(file.getPath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Final grades exported successfully.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void handleExportPassed(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            gradeService.exportPdfPassedStudents(file.getPath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Passed students exported successfully.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void handleExportNeverLate(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            gradeService.exportPdfNeverLateStudents(file.getPath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Never late students exported successfully.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Select pdf location");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
    }
}
