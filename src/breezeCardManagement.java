import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


public class breezeCardManagement {

    @FXML
    public TableView<Card> tableView;

    @FXML
    public TableColumn<Card, String> columnCardNumber;

    @FXML
    public TableColumn<Card, String> columnValue;
    @FXML
    public TableColumn<Card, String> columnOwner;


    public ObservableList<Card> data;

    @FXML
    public CheckBox showsus;

    @FXML
    public TextField newVal;

    @FXML
    public TextField owner;
    private static Stage window;
    @FXML
    private TextField cardnumber;
    @FXML
    private TextField low;
    @FXML
    private TextField high;

    @FXML
    private TextField newUser;


    public static void display() throws Exception {
        Parent root = FXMLLoader.load(breezeCardManagement.class.getResource("breezeCardManagement.fxml"));

        try {

            window = new Stage();

            Scene scene = new Scene(root, 600, 500);

            window.setTitle("Breeze Card Management");
            window.setScene(scene);
            window.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("here is the exception");
        }

    }

    public void update(ActionEvent actionEvent) {
        ConnectionConfig con = new ConnectionConfig();
        String sql = getSql();
        System.out.println(sql);
        ResultSet rs = con.getResult(sql);
        ResultSet rs2 = con.getResult("select BreezecardNum from Conflict");
        String breezenum;
        data = FXCollections.observableArrayList();
        try {
            while(rs.next()){
                rs2.beforeFirst();
                boolean temp = false;
                //Iterate Row
                breezenum= rs.getString("BreezecardNum");
                while (rs2.next()) {
                    System.out.println(rs2.getString("BreezecardNum"));
                    if (breezenum.equals(rs2.getString("BreezecardNum"))){
                        temp = true;
                    }
                }

                if (temp) {
                    data.add(new Card(rs.getString("BreezeCardNum"), rs.getString("Value"),"Suspended"));
                } else {

                    data.add(new Card(rs.getString("BreezeCardNum"), rs.getString("Value"), rs.getString("BelongsTo")));
                }


            }

            System.out.println(columnCardNumber==null);
            columnCardNumber.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
            columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
            columnOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));

            tableView.setItems(null);

            tableView.setItems(data);
        } catch (SQLException e) {
            System.out.println("sql exception");
            e.printStackTrace();
        }
        con.close();


    }

    private String getSql() {
        boolean ownerb = owner.getText().length() != 0;
        boolean cardnumberb = cardnumber.getText().length() != 0;
        boolean lowb = low.getText().length()!=0;
        boolean highb = high.getText().length() !=0;
        boolean showb = showsus.isSelected();


        String sql = "Select * from Breezecard";

        if ((ownerb || cardnumberb) ||(lowb || (highb|| showb) )) {
            sql += " where ";


            if (owner.getText().length() != 0) {
                sql += " BelongsTo = '" + owner.getText() + "' and ";
            }
            if (cardnumber.getText().length() != 0) {

                sql += " BreezecardNum = '" + cardnumber.getText() + "' and ";
            }
            if (low.getText().length() != 0) {
                sql += "value >='" + low.getText() + "' and ";

            }
            if (high.getText().length()!=0) {
                sql += "value <='" + high.getText() + "' and ";
            }


            if (!showsus.isSelected()) {

                sql += " NOT \n" +
                        "EXISTS (\n" +
                        "SELECT 1 \n" +
                        "FROM Conflict\n" +
                        "WHERE Conflict.BreezecardNum = Breezecard.BreezecardNum\n" +
                        ") and ";
            }
            sql = sql.substring(0, sql.length()-4);
        }


        System.out.println(sql);
        return sql;
    }

    public void reset(ActionEvent actionEvent) {
        owner.clear();
        cardnumber.clear();
        low.clear();
        high.clear();
        showsus.setSelected(false);
    }

    public void setValue(ActionEvent actionEvent) {
        double val = Double.parseDouble(newVal.getText().trim());

        if (val > 1000) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Value could not exceed 1000!");
            alert.showAndWait();
        } else if (val < 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Value could not be less than 0!");
            alert.showAndWait();

        } else {
            System.out.println("herer");
            Card card = tableView.getSelectionModel().getSelectedItem();
            System.out.println(card.getCardNumber());
            String bnum = card.getCardNumber();
        // Update Breezecard SET Value = 'val' where BreezecardNum = ''
            String sql = "Update Breezecard SET Value = " + Double.toString(val) + " where BreezecardNum = " + bnum;
            System.out.println(sql);
            ConnectionConfig con = new ConnectionConfig();
            try {
                con.update(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            update(new ActionEvent());
            con.close();


        }



    }

    public void transfer(ActionEvent actionEvent) {
        String user = newUser.getText().trim();
        ConnectionConfig con = new ConnectionConfig();
        ResultSet rs = con.getResult("Select * from USER where Username = '" + user + "' and IsAdmin = 1");
        ResultSet rs3 = con.getResult("Select * from USER where Username = '" + user + "'");
        System.out.println("Select * from USER where Username = '" + user + "'");
        try {
            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot assign the card to admin!");
                alert.showAndWait();
            } else if (!rs3.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Have to assign the card to some one in the system. ");
                alert.showAndWait();
            }
            else {
                // check if it is in conflict
                Card card = tableView.getSelectionModel().getSelectedItem();
                System.out.println(card.getCardNumber());
                String bnum = card.getCardNumber();
                ResultSet rs2 = con.getResult("Select * from Conflict where BreezecardNum = " + bnum);
                System.out.println("Select * from Conflict where BreezecardNum = " + bnum);
                //System.out.println(rs2.getString("BreezecardNum"));
                System.out.println("first");
                System.out.println(rs2.first());
                if (rs2.first()) {
                    System.out.println("here");
                    String sql = "Update Breezecard SET BelongsTo = '" + user + "' where BreezecardNum = " + bnum;
                    con.update(sql);
                    rs2.beforeFirst();
                    while(rs2.next()) {
                        String name = rs2.getString("Username");
                        ResultSet rs4 = con.getResult("Select * from Breezecard where BelongsTo = '"+ name + "'" );
                        if (!rs4.next()) {
                            //generate a new card for the user
                            boolean IsDuplicated = true;
                            long cardNum = 0;
                            while (IsDuplicated) {

                                Random random = new Random();
                                int rand16Digt = random.nextInt(16);


                                String strNum = Long.toString(rand16Digt);
                                ResultSet rs5 = con.getResult("Select * from Breezecard where BreezecardNum = '" + strNum + "'");
                                if (!rs5.next()) {   // The card number doesn't exist
                                    IsDuplicated = false;
                                }
                            }
                            String strNum = Long.toString(cardNum);
                            con.update("Insert into Breezecard(BreezecardNum,BelongsTo) Values('" + strNum + "','" + name + "')");

                        }
                        System.out.println("Delete from Conflict where BreezecardNum = '" + bnum + "'");
                        con.update("Delete from Conflict where BreezecardNum = '" + bnum + "'" );



                    }
                    try {
                        con.update(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //update(new ActionEvent());


                } else {
                    //not in conflict

                    String sql = "Update Breezecard SET BelongsTo = '" + user + "' where BreezecardNum = " + bnum;
                    System.out.println(sql);
                    ConnectionConfig con3 = new ConnectionConfig();
                    try {
                        con3.update(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    //update(new ActionEvent());

                }


            }
            update(new ActionEvent());
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }



}
