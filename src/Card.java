import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Card {
    private final StringProperty cardNumber;
    private final StringProperty value;
    private final StringProperty owner;

    //Default constructor
    public Card(String cardNumber, String value, String owner) {
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.value = new SimpleStringProperty(value);
        this.owner = new SimpleStringProperty(owner);
    }
    public Card(String cardNumber, String value) {
        this.cardNumber = new SimpleStringProperty(cardNumber);
        this.value = new SimpleStringProperty(value);
        this.owner = null;
    }

    //Getters
    public String getCardNumber() {
        return cardNumber.get();
    }

    public String getValue() {
        return value.get();
    }

    public String getOwner() {
        return owner.get();
    }

    //Setters
    public void setCardNumber(String value) {
        cardNumber.set(value);
    }

    public void setValue(String value2) {
        value.set(value2);
    }

    public void setOwner(String value) {
        owner.set(value);
    }

    //Property values
    public StringProperty cardNumberProperty() {
        return cardNumber;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public StringProperty ownerProperty() {
        return owner;
    }
}
