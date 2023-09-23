package controller;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import model.Appointment;
import utility.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class Analytics implements Initializable {

    public Label yearLabel;
    public MaterialIconView arrowForward;
    public MaterialIconView arrowBack;
    @FXML
    private Circle accountCircle;

    @FXML
    private Label accountText;

    @FXML
    private MaterialIconView analytics;

    @FXML
    private BarChart<?, ?> chart;

    @FXML
    private MaterialIconView dashboard;

    @FXML
    private DatePicker end;

    @FXML
    private MaterialIconView exit;

    @FXML
    private ComboBox<?> metricBox;

    @FXML
    private Label notifications;

    @FXML
    private MaterialIconView schedule;

    @FXML
    private MaterialIconView services;

    @FXML
    private DatePicker start;

    boolean upcoming = false;
    int year = LocalDate.now().getYear();

    @FXML
    void onAccount(MouseEvent event) { Alerts.logout(event); }

    @FXML
    void onAnalytics(MouseEvent event) {
        UI.load(UI.analytics, event);
    }

    @FXML
    void onDashboard(MouseEvent event) {
        UI.load(UI.dashboard, event);
    }

    @FXML
    void onExit(MouseEvent event) {
        Alerts.exit();
    }

    @FXML
    void onMetric(ActionEvent event) {

    }

    @FXML
    void onNotifications(MouseEvent event) {
        if (upcoming) {
            for (Appointment appointment : AppointmentQuery.select()) {
                if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(30))) {
                    Alerts.upcoming(appointment.getCustomerID(), appointment.getStart().toLocalDate(), appointment.getStart().toLocalTime());
                    break;
                }
            }
        } else {
            Alerts.noUpcoming();
        }
    }

    @FXML
    void onSchedule(MouseEvent event) {
        UI.load(UI.schedule, event);
    }

    @FXML
    void onServices(MouseEvent event) {
        UI.load(UI.services, event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountText.setText(Login.firstLetter);
        hideYear();
        ObservableList metrics = FXCollections.observableArrayList();
        metrics.add(0, "Appointments by Month");
        metrics.add(1, "Customers by Platform");
        metrics.add(2, "Customers by Service");
        metrics.add(3, "Customers by Country");
        metricBox.setItems(metrics);

        metricBox.getSelectionModel().selectedItemProperty().addListener((observableValue, priorSelection, newSelection) -> {
            if (newSelection != null) {
                int index = metricBox.getSelectionModel().getSelectedIndex();
                switch (index) {
                    case 0: appointmentsByMonth();
                    break;
                    case 1: customersByPlatform();
                    break;
                    case 2: customersByService();
                    break;
                    case 3: customersByCountry();
                    break;
                }

            }
        });

        // Upcoming Notification Checker
        for (Appointment appointment : AppointmentQuery.select()) {
            if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(30))) {
                notifications.setText("1 Notification");
                notifications.setStyle("-fx-text-fill: #63A58D");
                upcoming = true;
                break;
            }
        }

    }

    public void appointmentsByMonth() {
        displayYear();
        chart.getData().clear();
        XYChart.Series month = new XYChart.Series();
        month.getData().add(new XYChart.Data("January", AppointmentQuery.monthlyTotal(1, year)));
        month.getData().add(new XYChart.Data("February", AppointmentQuery.monthlyTotal(2, year)));
        month.getData().add(new XYChart.Data("March", AppointmentQuery.monthlyTotal(3, year)));
        month.getData().add(new XYChart.Data("April", AppointmentQuery.monthlyTotal(4, year)));
        month.getData().add(new XYChart.Data("May", AppointmentQuery.monthlyTotal(5, year)));
        month.getData().add(new XYChart.Data("June", AppointmentQuery.monthlyTotal(6, year)));
        month.getData().add(new XYChart.Data("July", AppointmentQuery.monthlyTotal(7, year)));
        month.getData().add(new XYChart.Data("August", AppointmentQuery.monthlyTotal(8, year)));
        month.getData().add(new XYChart.Data("September", AppointmentQuery.monthlyTotal(9, year)));
        month.getData().add(new XYChart.Data("October", AppointmentQuery.monthlyTotal(10, year)));
        month.getData().add(new XYChart.Data("November", AppointmentQuery.monthlyTotal(11, year)));
        month.getData().add(new XYChart.Data("December", AppointmentQuery.monthlyTotal(12, year)));
        chart.getData().addAll(month);
    }

    public void customersByPlatform() {
        hideYear();
        chart.getData().clear();
        XYChart.Series platform = new XYChart.Series();
        platform.getData().add(new XYChart.Data("Desktop", PlatformQuery.totalPlatforms("Desktop")));
        platform.getData().add(new XYChart.Data("Mobile", PlatformQuery.totalPlatforms("Mobile")));
        platform.getData().add(new XYChart.Data("Web", PlatformQuery.totalPlatforms("Web")));
        platform.getData().add(new XYChart.Data("Mixed Reality", PlatformQuery.totalPlatforms("Mixed Reality")));
        platform.getData().add(new XYChart.Data("Embedded", PlatformQuery.totalPlatforms("Embedded")));
        chart.getData().addAll(platform);
    }

    public void customersByService() {
        hideYear();
        chart.getData().clear();
        XYChart.Series service = new XYChart.Series();
        service.getData().add(new XYChart.Data("Custom Application Development", ServiceQuery.totalServices("Custom Application Development")));
        service.getData().add(new XYChart.Data("Cloud Computing", ServiceQuery.totalServices("Cloud Computing")));
        service.getData().add(new XYChart.Data("IT Support / Maintenance", ServiceQuery.totalServices("IT Support / Maintainence")));
        service.getData().add(new XYChart.Data("Blockchain", ServiceQuery.totalServices("Blockchain")));
        service.getData().add(new XYChart.Data("UI / UX Design", ServiceQuery.totalServices("UI / UX Design")));
        service.getData().add(new XYChart.Data("Software Testing QA", ServiceQuery.totalServices("Software Testing QA")));
        service.getData().add(new XYChart.Data("Data Science", ServiceQuery.totalServices("Data Science")));
        service.getData().add(new XYChart.Data("IoT", ServiceQuery.totalServices("IoT")));
        service.getData().add(new XYChart.Data("MVP", ServiceQuery.totalServices("MVP")));
        chart.getData().addAll(service);
    }

    public void customersByCountry() {
        hideYear();
        chart.getData().clear();
        XYChart.Series country= new XYChart.Series();
        country.getData().add(new XYChart.Data("US", CustomerQuery.total(1)));
        country.getData().add(new XYChart.Data("UK", CustomerQuery.total(2)));
        country.getData().add(new XYChart.Data("Canada", CustomerQuery.total(3)));
        chart.getData().addAll(country);
    }

    public void onForward(MouseEvent event) {
        year++;
        yearLabel.setText(String.valueOf(year));
        appointmentsByMonth();
    }

    public void onBack(MouseEvent event) {
        year--;
        yearLabel.setText(String.valueOf(year));
        appointmentsByMonth();
    }

    public void hideYear() {
        arrowForward.setVisible(false);
        arrowBack.setVisible(false);
        yearLabel.setVisible(false);
    }

    public void displayYear() {
        yearLabel.setText(String.valueOf(year));
        arrowForward.setVisible(true);
        arrowBack.setVisible(true);
        yearLabel.setVisible(true);
    }
}
