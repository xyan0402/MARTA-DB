package ConflictCard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConflictCard  {
    private final StringProperty cardNumber;
    private final StringProperty newOwner;
    private final StringProperty DateSuspended;
    private final StringProperty previousOwner;


    public ConflictCard(String cardNumber, String newOwner, String DateSuspended, String previousOwner) {
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.newOwner = new SimpleStringProperty(newOwner);
        this.DateSuspended = new SimpleStringProperty(DateSuspended);
        this.previousOwner = new SimpleStringProperty(previousOwner);

    }

    //Getters
    public String getCardNumber() {
        return cardNumber.get();
    }

    public String getNewOwner() {
        return newOwner.get();
    }

    public String getDateSuspended() {
        return DateSuspended.get();
    }
    public String getPreviousOwner() {
        return previousOwner.get();
    }

    //setter

    public void setCardNumber(String value) {
        cardNumber.set(value);
    }

    public void setNewOwner(String value2) {
        newOwner.set(value2);
    }

    public void setDateSuspended(String value) {
        DateSuspended.set(value);
    }

    public void setPreviousOwner(String value) {previousOwner.set(value);}

    //Property values
    public StringProperty cardNumberProperty() {
        return cardNumber;
    }

    public StringProperty newOwnerProperty() {
        return newOwner;
    }

    public StringProperty dateSuspendedProperty() {
        return DateSuspended;
    }

    public StringProperty previousOwnerProperty() {return previousOwner;}

}
