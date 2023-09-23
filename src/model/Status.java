package model;

public class Status {
    private int statusID;
    private String status;

    public Status(int statusID, String status) {
        this.statusID = statusID;
        this.status = status;
    }

    public int getStatusID() {
        return statusID;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() { return status; }
}
