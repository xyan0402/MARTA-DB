

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Record {
    private final StringProperty Name;
    private final StringProperty PassengerIn;
    private final StringProperty PassengerOut;
    private final StringProperty res;
    private final StringProperty Revenue;


    //Default constructor
    public Record(String Name, String PassengerIn, String PassengerOut, String res, String Revenue) {
        this.Name = new SimpleStringProperty(Name);
        this.PassengerIn = new SimpleStringProperty(PassengerIn);
        this.PassengerOut = new SimpleStringProperty(PassengerOut);
        this.res = new SimpleStringProperty(res);
        this.Revenue = new SimpleStringProperty(Revenue);
    }

    //Getters
    public String getName() {
        return Name.get();
    }

    public String getPassengerIn() {
        return PassengerIn.get();
    }

    public String getPassengerOut() {
        return PassengerOut.get();
    }

    public String getRes() {
        return res.get();
    }

    public String getRevenue() {
        return Revenue.get();
    }
    //Setters
    public void setName(String value) {
        Name.set(value);
    }

    public void setPassengerIn(String value2) {
        PassengerIn.set(value2);
    }

    public void setPassengerOut(String value) {
        PassengerOut.set(value);
    }
    public void setRes(String value) {res.set(value);}

    public void setRevenue(String status) {
        Revenue.set(status);
    }

    //Property values
    public StringProperty NameProperty() {
        return Name;
    }

    public StringProperty PassengerInProperty() {
        return PassengerIn;
    }

    public StringProperty PassengerOutProperty() {
        return PassengerOut;

    }
    public StringProperty resProperty() {
        return res;
    }
    public StringProperty RevenueProperty() {
        return Revenue;
    }
}


