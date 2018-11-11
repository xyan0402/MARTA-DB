import javafx.beans.property.*;

public class ReportInfo {
    private final StringProperty StationName;
    private final SimpleIntegerProperty PassengerIn;
    private final SimpleIntegerProperty PassengerOut;
    private final SimpleIntegerProperty Flow;
    private final SimpleDoubleProperty Revenue;

    // Default constructor
    public ReportInfo(String StationName, int PassengerIn, int PassengerOut, int Flow, double Revenue) {
        this.StationName = new SimpleStringProperty(StationName);
        this.PassengerIn = new SimpleIntegerProperty(PassengerIn);
        this.PassengerOut = new SimpleIntegerProperty(PassengerOut);
        this.Flow = new SimpleIntegerProperty(Flow);
        this.Revenue = new SimpleDoubleProperty(Revenue);
    }

    // Getters
    public String getStationName () {
        return StationName.get();
    }

    public int getPassengerIn () {
        return PassengerIn.get();
    }

    public int getPassengerOut () {
        return PassengerOut.get();
    }

    public int getFlow () {
        return Flow.get();
    }

    public double getRevenue () {
        return Revenue.get();
    }

    // Setters
    public void setStationName(String value) {
        StationName.set(value);
    }

    public void setPassengerIn(int value) {
        PassengerIn.set(value);
    }

    public void setPassengerOut(int value) {
        PassengerOut.set(value);
    }

    public void setFlow(int value) {
        Flow.set(value);
    }

    public void setRevenue(double value) {
        Revenue.set(value);
    }

    // Property Values
    public StringProperty StationNameProperty() {
        return StationName;
    }

    public IntegerProperty PassengerInProperty() {
        return PassengerIn;
    }

    public IntegerProperty PassengerOutProperty() {
        return PassengerOut;
    }

    public IntegerProperty FlowProperty() {
        return Flow;
    }

    public DoubleProperty RevenueProperty() {
        return Revenue;
    }

}

