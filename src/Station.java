

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Station {
    private final StringProperty stationName;
    private final StringProperty stopID;
    private final StringProperty Fare;
    private final StringProperty Status;


    //Default constructor
    public Station(String stationName, String stopID, String Fare, String Status) {
        this.stationName = new SimpleStringProperty(stationName);
        this.stopID = new SimpleStringProperty(stopID);
        this.Fare = new SimpleStringProperty(Fare);
        this.Status = new SimpleStringProperty(Status);
    }

    //Getters
    public String getStationName() {
        return stationName.get();
    }

    public String getStopID() {
        return stopID.get();
    }

    public String getFare() {
        return Fare.get();
    }

    public String getStatus() {
        return Status.get();
    }
    //Setters
    public void setStationName(String value) {
        stationName.set(value);
    }

    public void setStopID(String value2) {
        stopID.set(value2);
    }

    public void setFare(String value) {
        Fare.set(value);
    }
    public void setStatus(String status) {
        Status.set(status);
    }

    //Property values
    public StringProperty stationNameProperty() {
        return stationName;
    }

    public StringProperty stopIDProperty() {
        return stopID;
    }

    public StringProperty fareProperty() {
        return Fare;

    }
    public StringProperty statusProperty() {
        return Status;
    }
}


