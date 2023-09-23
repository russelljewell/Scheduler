package controller;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import model.Appointment;
import model.Contact;
import utility.Alerts;
import utility.AppointmentQuery;
import utility.ContactQuery;
import utility.UI;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class Schedule implements Initializable {

    @FXML
    private Circle accountCircle;

    @FXML
    private Label accountText;

    @FXML
    private MaterialIconView analytics;

    @FXML
    private TableColumn<?, ?> apptIdCol;

    @FXML
    private TableView<Appointment> apptTable;

    @FXML
    private ComboBox<Contact> contactBox;

    @FXML
    private TableColumn<?, ?> custIdCol;

    @FXML
    private MaterialIconView dashboard;

    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private TableColumn<?, ?> endCol;

    @FXML
    private MaterialIconView exit;

    @FXML
    private TableColumn<?, ?> locCol;

    @FXML
    private Label notifications;

    @FXML
    private MaterialIconView schedule;

    @FXML
    private MaterialIconView services;

    @FXML
    private TableColumn<?, ?> startCol;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<?, ?> userIdCol;

    boolean upcoming = false;
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

        // Upcoming Notification Checker
        for (Appointment appointment : AppointmentQuery.select()) {
            if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(30))) {
                notifications.setText("1 Notification");
                notifications.setStyle("-fx-text-fill: #63A58D");
                upcoming = true;
                break;
            }
        }

        // Initialize Appointment Table
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));


        contactBox.setItems(ContactQuery.contacts());
        contactBox.getSelectionModel().selectedItemProperty().addListener((observableValue, priorSelection, newSelection) -> {
            if (newSelection != null) {
                for (Contact contact : contactBox.getItems()) {
                    if (contact.getContactID() == contactBox.getValue().getContactID()) {
                        apptTable.setItems(AppointmentQuery.report(contactBox.getValue().getContactID()));
                        apptTable.getSortOrder().add((TableColumn<Appointment, ?>) startCol);
                        apptTable.sort();
                    }
                }
            }
        });
    }
}
