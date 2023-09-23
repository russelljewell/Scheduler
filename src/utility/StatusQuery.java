package utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusQuery {

    public static ObservableList<Status> status() {
        ObservableList allStatuses = FXCollections.observableArrayList();
        String sql = "SELECT * FROM STATUS";
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int statusID = rs.getInt("Status_ID");
                String statusName = rs.getString("Status");
                Status status = new Status(statusID, statusName);
                allStatuses.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allStatuses;
    }
}
