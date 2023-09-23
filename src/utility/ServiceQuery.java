package utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ServiceQuery {

    public static int setService(String service, int customerID) {
        String sql = "INSERT IGNORE INTO SERVICES (Customer_ID, Service) VALUES (?, ?)";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setString(2, service);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static int removeService(String service, int customerID) {
        String sql = "DELETE FROM SERVICES WHERE Service = ? AND Customer_ID = ?";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, service);
            ps.setInt(2, customerID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static ObservableList<Service> getServices(int customerID) {
        ObservableList<Service> services = FXCollections.observableArrayList();
        String sql = "SELECT Service FROM SERVICES WHERE Customer_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String serviceName = rs.getString("Service");
                Service service = new Service(customerID, serviceName);
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public static int totalServices(String serviceName) {
        String sql = "SELECT COUNT(*) AS total FROM SERVICES WHERE Service = ?";
        int total = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, serviceName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
