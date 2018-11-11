import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ManageCards {
    @FXML
    private TableView<Card> cardlist;
    @FXML
    private TextField cardnum;
    @FXML
    private TextField chargecard;
    @FXML
    private TextField chargevalue;
    @FXML
    private Button addcard;
    @FXML
    private Button removecard;
    @FXML
    private Button addvalue;
    @FXML
    private Button see;
    @FXML
    private TableColumn<Card,String> cardnumbercol;
    @FXML
    private TableColumn<Card,String> cardvaluecol;
    @FXML
    private Label succeed;
    @FXML
    private Label succeedfund;

    public ObservableList<Card> data;

    private static Stage window;
    private static String Tname;

    public static void display(String name){
        try {
            Tname = name;
            Parent root = FXMLLoader.load(ManageCards.class.getResource("ManageCards.fxml"));

            window = new Stage();
            Scene scene = new Scene(root);
            window.setTitle("Manage Cards");
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            System.out.println("Exception1:" + e.getMessage());
        }
    }

    public void refresh(){
        ConnectionConfig con=new ConnectionConfig();
        ResultSet rs = con.getResult("Select BreezecardNum, Value from Breezecard where BelongsTo = " + "'" + Tname + "'");
        data = FXCollections.observableArrayList();
        try {
            if(!rs.next()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No card found");
                alert.showAndWait();
                return;
            }else{
                rs.beforeFirst();
            }
            while (rs.next()) {
                String cardnumber = rs.getString("BreezecardNum").trim();
                String cardvalue=rs.getString("Value").trim();
                ResultSet rs1 = con.getResult("Select * from Conflict where BreezecardNum =" + "'" + cardnumber + "'");
                if (!rs1.next()) {
                    data.add(new Card(cardnumber,cardvalue));
                }
            }

        } catch (Exception f) {
            System.err.println("Exception2: " + f.getMessage());
            //System.out.println("111");
        }
        con.close();
        cardnumbercol.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        cardvaluecol.setCellValueFactory(new PropertyValueFactory<>("value"));
        cardlist.setItems(null);
        cardlist.setItems(data);
    }

    public void cliktable(){
        succeed.setVisible(false);
        Card card = cardlist.getSelectionModel().getSelectedItem();
        if(card!=null) {
            String bnum = card.getCardNumber();
            cardnum.setText(bnum);
            chargecard.setText(bnum);
        }
    }

    public void addcardto(){
        succeed.setVisible(false);
        succeedfund.setVisible(false);
        String bum=cardnum.getText().trim();
//        if(bum==null){
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Error");
//            alert.setHeaderText("Please fill in the card number");
//            alert.showAndWait();
//            return;
//        }
        if(bum.length()!=16){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong card number");
            alert.showAndWait();
            return;
        }
        ConnectionConfig con=new ConnectionConfig();
        ResultSet rs=con.getResult("Select BelongsTo from Breezecard Where BreezecardNum='"+bum+"'");
        try{
            if(!rs.next()){
                //not exist
                con.update("INSERT INTO Breezecard (BreezecardNum, BelongsTo) Values('"+bum+"','"+Tname+"')");
                try{
                    succeed.setVisible(true);
                }catch (Exception f){
                    System.err.println("Exception8"+f.getMessage());
                }
                System.out.println("Insert card "+bum+" belongs to "+Tname);
            }else {
                //if null
                System.out.println(rs.getString("BelongsTo"));
                if (rs.getString("BelongsTo") == null) {
                    System.out.println(rs.getString("BelongsTo"));
                    System.out.println("NULL card found");
                    con.update("UPDATE Breezecard set BelongsTo='"+Tname+"' where BreezecardNum='"+bum+"'");
                    try{
                        succeed.setVisible(true);
                    }catch (Exception f){
                        System.err.println("Exception8"+f.getMessage());
                    }
                    System.out.println("Update card "+bum+" belongs to "+Tname);
                } else {
                    if (rs.getString("BelongsTo").equals(Tname) ) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("You already own or claim to own this card");
                        alert.showAndWait();
                        return;
                    } else {
                        ResultSet rs1 = con.getResult("Select Username from Conflict where BreezecardNum='" + bum + "'");
                        try {
                            if (rs1.next()) {
                                rs1.beforeFirst();
                                boolean has = false;
                                while (rs1.next()) {
                                    if (rs1.getString("Username").equals(Tname)) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Error");
                                        alert.setHeaderText("You already own or claim to own this card");
                                        alert.showAndWait();
                                        has = true;
                                        return;
                                    }
                                }
                                if (!has) {
                                    con.update("INSERT INTO Conflict (BreezecardNum, Username) Values('" + bum + "','" + Tname + "')");
                                    try{
                                        succeed.setVisible(true);
                                    }catch (Exception f){
                                        System.err.println("Exception8"+f.getMessage());
                                    }
                                    System.out.println("1Insert conflict card " + bum + " to " + Tname);
                                }
                            } else {
                                con.update("INSERT INTO Conflict (BreezecardNum, Username) Values('" + bum + "','" + Tname + "')");
                                try{
                                    succeed.setVisible(true);
                                }catch (Exception f){
                                    System.err.println("Exception8"+f.getMessage());
                                }
                                System.out.println("2Insert conflict card " + bum + " to " + Tname);
                            }
                        } catch (Exception f) {
                            System.err.println("Exception4: " + f.getMessage());
                            //System.out.println("111");
                        }
                    }
                }
            }

        }catch (Exception f) {
            System.err.println("Exception3: " + f.getMessage());
            //System.out.println("111");
        }
        con.close();
        refresh();
    }

    public void removefrom(){
        succeed.setVisible(false);
        succeedfund.setVisible(false);
        String bum=cardnum.getText().trim();
//        if(bum==null){
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Error");
//            alert.setHeaderText("Please fill in the card number");
//            alert.showAndWait();
//            return;
//        }
        if(bum.length()!=16){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong card number");
            alert.showAndWait();
            return;
        }
        ConnectionConfig con=new ConnectionConfig();
        ResultSet rs=con.getResult("Select BelongsTo from Breezecard where BreezecardNum='"+bum+"'");
        try{
            if(!rs.next()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Card Number not found");
                alert.showAndWait();
                return;
            }else{
                if(rs.getString("BelongsTo").equals(Tname)){
                    ResultSet rs1=con.getResult("Select COUNT(*) from Breezecard where BelongsTo='"+Tname+"'");
                    rs1.next();
                    System.out.println(rs1.getString("COUNT(*)"));
                    rs1.beforeFirst();
                    try{
                        if(!rs1.next()){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("You have no cards!");
                            alert.showAndWait();
                            return;
                        }else if(Integer.parseInt(rs1.getString("COUNT(*)").trim())<=2){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("You cannot remove your last card!");
                            alert.showAndWait();
                            return;
                        }
                    }catch (Exception f){
                        System.err.println("Exception8"+f.getMessage());
                    }
                    con.update("UPDATE Breezecard set BelongsTO=NULL where BreezecardNum='"+bum+"'");
                    try{
                        succeed.setVisible(true);
                    }catch (Exception f){
                        System.err.println("Exception8"+f.getMessage());
                    }
                    System.out.println("Remove the card");

                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Can not remove this card: This is conflict card or this is not your card");
                    alert.showAndWait();
                    return;
                }
            }
        }catch (Exception f){
            System.err.println("Exception5: " + f.getMessage());
        }
        con.close();
        refresh();
    }

    public void addfund(){
        succeedfund.setVisible(false);
        String bnum=chargecard.getText().trim();
//        if(bnum==null){
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Error");
//            alert.setHeaderText("Please fill in the card number");
//            alert.showAndWait();
//            return;
//        }
        if(bnum.length()!=16){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong card number");
            alert.showAndWait();
            return;
        }
        String value=chargevalue.getText().trim();
        if(value==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in the card number");
            alert.showAndWait();
            return;
        }
        double chargenumber=0.0;
        try{
            chargenumber=Double.valueOf(value.trim());
        }catch (Exception f){
            System.err.println("Exception11: " + f.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Value is not a number");
            alert.showAndWait();
            return;
        }
        ConnectionConfig con=new ConnectionConfig();
        ResultSet rs=con.getResult("Select Value from Breezecard where BelongsTo='"+Tname+"' and BreezecardNum='"+bnum+"'");
        try{
            if(!rs.next()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("You don't own this card");
                alert.showAndWait();
                return;
            }else{
                double orivalue=Double.valueOf(rs.getString("Value").trim());
                double lastvalue=orivalue+chargenumber;
                if(lastvalue>1000.0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("The balance of this card will exceed $1000\n you cannot charge this card");
                    alert.showAndWait();
                    return;
                }else{
                    con.update("UPDATE Breezecard set Value="+lastvalue+" where BreezecardNum='"+bnum+"'");
                    try{
                        succeedfund.setVisible(true);
                    }catch (Exception f){
                        System.err.println("Exception10: " + f.getMessage());}
                }
            }
        }catch (Exception f){
            System.err.println("Exception10: " + f.getMessage());
        }
        con.close();
        refresh();
     }

}
