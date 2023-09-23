package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import model.*;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import utility.*;


import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class Services implements Initializable {

    public Label statusLabel;
    public Label phoneLabel;
    public Label appointmentLabel;
    public JFXCheckBox checkCustomApplication;
    public JFXCheckBox checkCloud;
    public JFXCheckBox checkIT;
    public JFXCheckBox checkBlockchain;
    public JFXCheckBox checkDesign;
    public JFXCheckBox checkQA;
    public JFXCheckBox checkDataScience;
    public JFXCheckBox checkIOT;
    public JFXCheckBox checkMVP;
    public JFXCheckBox checkDesktop;
    public JFXCheckBox checkMobile;
    public JFXCheckBox checkWeb;
    public JFXCheckBox checkVirtual;
    public JFXCheckBox checkEmbedded;

    int customerID = 0;
    @FXML
    private Circle accountCircle;

    @FXML
    private Label accountText;

    @FXML
    private Label addressText;

    @FXML
    private MaterialIconView analytics;

    @FXML
    private Label appointmentText;

    @FXML
    private Label countryText;

    @FXML
    private MaterialIconView dashboard;

    @FXML
    private Label divisionText;

    @FXML
    private MaterialIconView exit;

    @FXML
    private Label nameText;

    @FXML
    private JFXTextArea notes;

    @FXML
    private Label notifications;

    @FXML
    private Label phoneText;

    @FXML
    private Label postalText;

    @FXML
    private MaterialIconView schedule;

    @FXML
    private CustomTextField search;

    @FXML
    private MaterialIconView services;

    @FXML
    private JFXComboBox<Status> statusBox;
    boolean upcoming = false;
    ObservableList<CheckBox> serviceCheckBoxes = FXCollections.observableArrayList();
    ObservableList<CheckBox> platformCheckBoxes = FXCollections.observableArrayList();




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
    void onReset(MouseEvent event) {
        for (CheckBox checkBox : serviceCheckBoxes) {
            checkBox.setSelected(false);
        }
        for (CheckBox checkBox : platformCheckBoxes) {
            checkBox.setSelected(false);
        }
        for (Customer customer : CustomerQuery.select()) {
            if (search.getText().contains(customer.getCustomerName())) {
                notes.setText(customer.getNotes());
                checkBoxGetter(customer.getCustomerID());
                for (Status status : statusBox.getItems()) {
                    if (customer.getStatusID() == status.getStatusID()) {
                        statusBox.setValue(status);
                    }
                }
            }
        }
    }

    @FXML
    void onSave(MouseEvent event) {
        if (statusBox.getValue() != null) {
            CustomerQuery.updateStatus(statusBox.getValue().getStatusID(), customerID);
        }
        checkBoxSetter(customerID);
        CustomerQuery.updateNotes(notes.getText(), customerID);
        nameText.setText("Changes saved.");
        clearUI();
        search.clear();
    }

    @FXML
    void onSearch(ActionEvent event) {
        clearUI();
        for (Customer customer : CustomerQuery.select()) {
            if (search.getText().contains(customer.getCustomerName())) {
                statusLabel.setVisible(true);
                statusBox.setVisible(true);
                phoneLabel.setVisible(true);
                appointmentLabel.setVisible(true);
                customerID = customer.getCustomerID();
                nameText.setText(customer.getCustomerName());
                addressText.setText(customer.getAddress());
                divisionText.setText(customer.getDivisionName());
                countryText.setText(customer.getCountryName());
                postalText.setText(customer.getPostalCode());
                phoneText.setText(customer.getPhoneNumber());
                notes.setText(customer.getNotes());
                if (AppointmentQuery.nextAppointment(customer.getCustomerID()) == null) {
                    appointmentText.setText("None");
                } else {
                    appointmentText.setText(String.valueOf(AppointmentQuery.nextAppointment(customer.getCustomerID())));
                }
                checkBoxGetter(customer.getCustomerID());
                for (Status status : statusBox.getItems()) {
                    if (customer.getStatusID() == status.getStatusID()) {
                        statusBox.setValue(status);
                    }
                }
            }
        }
    }

    @FXML
    void onServices(MouseEvent event) { UI.load(UI.services, event); }
    @FXML
    void onSchedule(MouseEvent event) { UI.load(UI.schedule, event); }
    @FXML
    void onAnalytics(MouseEvent event) { UI.load(UI.analytics, event); }
    @FXML
    void onDashboard(MouseEvent event) { UI.load(UI.dashboard, event); }
    @FXML
    void onExit(MouseEvent event) { Alerts.exit(); }
    @FXML
    void onAccount(MouseEvent event) { Alerts.logout(event); }

    public void checkBoxSetter(int customerID) {
        for (CheckBox checkBox : serviceCheckBoxes) {
            if (checkBox.isSelected()) {
                ServiceQuery.setService(checkBox.getText(), customerID);
            } else {
                ServiceQuery.removeService(checkBox.getText(), customerID);
            }
        }
        for (CheckBox checkBox : platformCheckBoxes) {
            if (checkBox.isSelected()) {
                PlatformQuery.setPlatforms(checkBox.getText(), customerID);
            } else {
                PlatformQuery.removePlatform(checkBox.getText(), customerID);
            }
        }
    }

    public void checkBoxGetter(int customerID) {
        for (Platform platform : PlatformQuery.getPlatforms(customerID)) {
            for (CheckBox checkBox : platformCheckBoxes) {
                if (platform.getPlatform().equals(checkBox.getText())) {
                    checkBox.setSelected(true);
                }
            }
        }
        for (Service service : ServiceQuery.getServices(customerID)) {
            for (CheckBox checkBox : serviceCheckBoxes) {
                if (service.getService().equals(checkBox.getText())) {
                    checkBox.setSelected(true);
                }
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clearUI();
        nameText.setText("No customer selected.");
        statusBox.setItems(StatusQuery.status());
        accountText.setText(Login.firstLetter);

        // Add service checkboxes to service checkbox array list
        serviceCheckBoxes.add(checkCustomApplication);
        serviceCheckBoxes.add(checkCloud);
        serviceCheckBoxes.add(checkIT);
        serviceCheckBoxes.add(checkBlockchain);
        serviceCheckBoxes.add(checkDesign);
        serviceCheckBoxes.add(checkQA);
        serviceCheckBoxes.add(checkDataScience);
        serviceCheckBoxes.add(checkIOT);
        serviceCheckBoxes.add(checkMVP);

        // Add platform checkboxes to platform checkbox array list
        platformCheckBoxes.add(checkDesktop);
        platformCheckBoxes.add(checkMobile);
        platformCheckBoxes.add(checkWeb);
        platformCheckBoxes.add(checkVirtual);
        platformCheckBoxes.add(checkEmbedded);


        // Upcoming Notification Checker
        for (Appointment appointment : AppointmentQuery.select()) {
            if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(30))) {
                notifications.setText("1 Notification");
                notifications.setStyle("-fx-text-fill: #63A58D");
                upcoming = true;
                break;
            }
        }

        // Search autocomplete
        TextFields.bindAutoCompletion(search, CustomerQuery.customerSearch());
    }

    public void clearUI() {
        statusLabel.setVisible(false);
        statusBox.setVisible(false);
        phoneLabel.setVisible(false);
        appointmentLabel.setVisible(false);
        addressText.setText("");
        divisionText.setText("");
        countryText.setText("");
        postalText.setText("");
        phoneText.setText("");
        appointmentText.setText("");
        notes.clear();
        for (CheckBox checkBox : serviceCheckBoxes) {
            checkBox.setSelected(false);
        }
        for (CheckBox checkBox : platformCheckBoxes) {
            checkBox.setSelected(false);
        }
        notes.clear();
    }
}
