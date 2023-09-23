package utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Platform;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class PlatformQuery {
    public static int setPlatforms(String platform, int customerID) {
        String sql = "INSERT IGNORE INTO PLATFORMS (Customer_ID, Platform) VALUES (?, ?) ";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setString(2, platform);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static int removePlatform(String platform, int customerID) {
        String sql = "DELETE FROM PLATFORMS WHERE Platform = ? AND Customer_ID = ?";
        int rowsAffected = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, platform);
            ps.setInt(2, customerID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static ObservableList<Platform> getPlatforms(int customerID) {
        ObservableList<Platform> platforms = FXCollections.observableArrayList();
        String sql = "SELECT Platform FROM PLATFORMS WHERE Customer_ID = ?";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String platformName = rs.getString("Platform");
                Platform platform = new Platform(customerID, platformName);
                platforms.add(platform);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return platforms;
    }

    public static int totalPlatforms(String platformName) {
        String sql = "SELECT COUNT(*) AS total FROM PLATFORMS WHERE Platform = ?";
        int total = 0;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, platformName);
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
