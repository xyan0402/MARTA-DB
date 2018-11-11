import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class trip {
    private final StringProperty starttime;
    private final StringProperty startat;
    private final StringProperty endat;
    private final StringProperty fare;
    private final StringProperty breezenum;

    //Default constructor
    public trip(String starttime, String startat, String endat, String fare, String breezenum) {
        this.starttime = new SimpleStringProperty(starttime);
        this.startat = new SimpleStringProperty(startat);
        this.endat = new SimpleStringProperty(endat);
        this.fare = new SimpleStringProperty(fare);
        this.breezenum= new SimpleStringProperty(breezenum);
    }

    //Getters
    public String getstarttime() {
        return starttime.get();
    }

    public String getstartat() {
        return startat.get();
    }

    public String getendat() {
        return endat.get();
    }

    public String getfare(){
        return fare.get();
    }

    public String getbreezenum(){
        return breezenum.get();
    }

    //Setters
    public void setstarttime(String value) {
        starttime.set(value);
    }

    public void setstartat(String value) {
        startat.set(value);
    }

    public void setendat(String value) {
        endat.set(value);
    }

    public void setfare(String value){
        fare.set(value);
    }

    public void setbreezenum(String value){
        breezenum.set(value);
    }
    //Property values
    public StringProperty starttimeProperty() {
        return starttime;
    }

    public StringProperty startatProperty() {
        return startat;
    }

    public StringProperty endatProperty() {
        return endat;
    }
    public StringProperty fareProperty(){
        return fare;
    }

    public StringProperty breezenumProperty() {
        return breezenum;
    }
}
