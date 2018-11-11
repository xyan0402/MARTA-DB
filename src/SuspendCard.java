import ConflictCard.ConflictCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class SuspendCard {
    private static Stage window;
    public ObservableList<ConflictCard> data;

    @FXML
    public TableView<ConflictCard> tableView;

    @FXML
    public TableColumn<ConflictCard, String> colCardNum;

    @FXML
    public TableColumn<Card, String> colNewOwner;
    @FXML
    public TableColumn<Card, String> colDateSuspended;

    @FXML
    public TableColumn<Card, String> colPreviousOwner;






    public static void display() {
        Parent root = null;
        try {
            root = FXMLLoader.load(SuspendCard.class.getResource("SuspendCard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        window = new Stage();

        Scene scene = new Scene(root, 600, 500);

        window.setTitle("Manager");
        window.setScene(scene);
        window.show();
    }

    public void newOwner(ActionEvent actionEvent) {
        ConflictCard card = tableView.getSelectionModel().getSelectedItem();

        String bnum = card.getCardNumber();
        String newOwner = card.getNewOwner();
        String oldOwner = card.getPreviousOwner();
        String update = "Update Breezecard SET BelongsTo = '" + newOwner+"' where BreezecardNum = '"+bnum + "'";
        System.out.println(update);
        String sel = "Select * from Conflict where BreezecardNum = '" + bnum + "'";
        ConnectionConfig con = new ConnectionConfig();
        try {
            con.update(update);
            ResultSet rs = con.getResult(sel);
            while (rs.next()) {
                //loop through all the pre users, if they do not have card, generate a new card for them
                String preuser = rs.getString("Username");
                ResultSet rs2 = con.getResult("select * from Breezecard where BelongsTo ='"+ preuser+"'");
                if (!rs2.next()) {
                    //generate a new card and insert

                    boolean IsDuplicated = true;
                    long cardNum = 0;
                    while (IsDuplicated) {
                        Random random = new Random();
                        int rand16Digt = random.nextInt(16);


                        String strNum = Long.toString(rand16Digt);    // Convert long to string
                        // cardNum = char(0123456780987654);
                        ResultSet rs5 = con.getResult("Select * from Breezecard where BreezecardNum = '" + strNum + "'");
                        if (!rs5.next()) {   // The card number doesn't exist
                            IsDuplicated = false;
                        }
                    }
                    String strNum = Long.toString(cardNum);
                    con.update("Insert into Breezecard(BreezecardNum,BelongsTo) Values('" + strNum + "','" + preuser + "')");

                }
                System.out.println("select * from Breezecard where BelongsTo = '" + oldOwner +"'");
                ResultSet rs3 = con.getResult("select * from Breezecard where BelongsTo = '" + oldOwner +"'");
                if(!rs3.next()) {
                    //generate a new card to pre user
                    boolean IsDuplicated = true;
                    long cardNum = 0;
                    while (IsDuplicated) {

                        Random random = new Random();
                        int rand16Digt = random.nextInt(16);


                        String strNum = Long.toString(rand16Digt);    // Convert long to string
                        // cardNum = char(0123456780987654);
                        ResultSet rs5 = con.getResult("Select * from Breezecard where BreezecardNum = '" + strNum + "'");
                        if (!rs5.next()) {   // The card number doesn't exist
                            IsDuplicated = false;
                        }
                    }
                    String strNum = Long.toString(cardNum);
                    con.update("Insert into Breezecard(BreezecardNum,BelongsTo) Values('" + strNum + "','" + oldOwner + "')");


                }
                String del = "Delete from Conflict where BreezecardNum = '" + bnum + "'";
                System.out.println(del);
                con.update(del);
                con.close();
                refresh(new ActionEvent());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void previousOwner(ActionEvent actionEvent) { // need to double check
        ConflictCard card = tableView.getSelectionModel().getSelectedItem();

        String bnum = card.getCardNumber();
        String newOwner = card.getNewOwner();
        String oldOwner = card.getPreviousOwner();
        String update = "Update Breezecard SET BelongsTo = '" + oldOwner+"' where BreezecardNum = '"+bnum + "'";
        System.out.println(update);
        String sel = "Select * from Conflict where BreezecardNum = '" + bnum + "'";
        ConnectionConfig con = new ConnectionConfig();
        try {
            con.update(update);
            ResultSet rs = con.getResult(sel);
            while (rs.next()) {
                //loop through all the pre users, if they do not have card, generate a new card for them
                String preuser = rs.getString("Username");
                ResultSet rs2 = con.getResult("select * from Breezecard where BelongsTo ='"+ preuser+"'");
                if (!rs2.next()) {
                    //generate a new card and insert

                    boolean IsDuplicated = true;
                    long cardNum = 0;
                    while (IsDuplicated) {

//                        java.util.Random rng = new java.util.Random();
//                        cardNum = (rng.nextLong() % 10000000000000000L);  // ***cardNum and strNum*** is for get a new card!!!!!!
                        Random random = new Random();
                        int rand16Digt = random.nextInt(16);


                        String strNum = Long.toString(rand16Digt);    // Convert long to string
                        // cardNum = char(0123456780987654);
                        ResultSet rs5 = con.getResult("Select * from Breezecard where BreezecardNum = '" + strNum + "'");
                        if (!rs5.next()) {   // The card number doesn't exist
                            IsDuplicated = false;
                        }
                    }
                    String strNum = Long.toString(cardNum);
                    System.out.println(strNum.length());
                    con.update("Insert into Breezecard(BreezecardNum,BelongsTo) Values('" + strNum + "','" + preuser + "')");

                }
                String del = "Delete from Conflict where BreezecardNum = '" + bnum + "'";
                System.out.println(del);
                con.update(del);
                con.close();
                refresh(new ActionEvent());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void refresh(ActionEvent actionEvent) {
        ConnectionConfig con = new ConnectionConfig();
        ResultSet rs = con.getResult("SELECT * \n" +
                "FROM Breezecard\n" +
                "NATURAL JOIN Conflict");

        data = FXCollections.observableArrayList();

        try {
            while(rs.next()){

                data.add(new ConflictCard
                        (rs.getString("BreezeCardNum"),
                                rs.getString("Username"),
                                rs.getString("DateTime"),
                                rs.getString("BelongsTo")));



            }


            colCardNum.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
            colNewOwner.setCellValueFactory(new PropertyValueFactory<>("newOwner"));
            colDateSuspended.setCellValueFactory(new PropertyValueFactory<>("DateSuspended"));
            colPreviousOwner.setCellValueFactory(new PropertyValueFactory<>("previousOwner"));


            tableView.setItems(null);

            tableView.setItems(data);
        } catch (SQLException e) {
            System.out.println("sql exception");
            e.printStackTrace();
        }
        con.close();

    }
}
