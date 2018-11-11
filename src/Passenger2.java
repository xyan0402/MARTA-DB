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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Passenger2 {

    @FXML
    private ChoiceBox cb;
    @FXML
    private GridPane grid;


    @FXML
    private ChoiceBox cb1;

    @FXML
    private ChoiceBox cb2;
    @FXML
    private Label Balancenum;

    @FXML
    private Hyperlink managecards;
    @FXML
    private Hyperlink starttrip;
    @FXML
    private Hyperlink endtrip;

    @FXML
    private Button viewhistory;

    @FXML
    private Button Logout;
    @FXML
    private Label alreadytrip;
    @FXML
    private Button refresh;

    private static Stage window;
    private static String Tname;


    public static void display(String name) {
        try {
            Tname = name;
            Parent root = FXMLLoader.load(Passenger2.class.getResource("Passenger2.fxml"));

            window = new Stage();
            Scene scene = new Scene(root);
            window.setTitle("Passenger Functionality");
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            System.out.println("Exception1:" + e.getMessage());
        }
    }

    public void checktrip() {
        System.out.println("check");
        //        //choicebox of card number
        ArrayList cards = new ArrayList();
        ConnectionConfig con = new ConnectionConfig();
        ResultSet rs = con.getResult("Select BreezecardNum from Breezecard where BelongsTo = " + "'" + Tname + "'");
        System.out.println("Select BreezecardNum from Breezecard where BelongsTo = " + "'" + Tname + "'");
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
                String cardnumber = rs.getString("BreezecardNum".trim());
                ResultSet rs1 = con.getResult("Select * from Conflict where BreezecardNum =" + "'" + cardnumber + "'");
                if (!rs1.next()) {
                    cards.add(cardnumber);
                    System.out.println(cardnumber);
                }
            }

        } catch (Exception f) {
            System.err.println("Exception2: " + f.getMessage());
            //System.out.println("111");
        }
        cb.setItems(FXCollections.observableArrayList(cards));
        cb.setDisable(false);

        //choicebox of start at
        ArrayList station = new ArrayList();//name+bus+fare
        ArrayList stationname = new ArrayList(); //name+bus
        rs = con.getResult("Select Name, StopID, EnterFare, IsTrain from Station where ClosedStatus = False Order by Name");
        try {
            if(!rs.next()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("No station found");
                alert.showAndWait();
                return;
            }else{
                rs.beforeFirst();
            }
            while (rs.next()) {
                String stopid1 = rs.getString("StopID".trim());
                String istrain = rs.getString("IsTrain").trim();
                //System.out.println(istrain);
                if (istrain.contains("0")) {
                    String stationnameplus = rs.getString("Name".trim()) + "-BUS-$" + rs.getString("EnterFare".trim());
                    station.add(stationnameplus);
                    String staname = rs.getString("Name".trim()) + "-BUS";
                    stationname.add(staname);
                } else {
                    String stationnameplus = rs.getString("Name".trim()) + "-$" + rs.getString("EnterFare".trim());
                    station.add(stationnameplus);
                    String staname = rs.getString("Name".trim());
                    stationname.add(staname);
                }
            }

        } catch (Exception f) {
            System.err.println("Exception3: " + f.getMessage());
            //System.out.println("222");
        }
        cb1.setDisable(false);
        cb1.setItems(FXCollections.observableArrayList(station));
        cb1.setDisable(true);


        //check if in trip

        boolean intrip = false;
        String cardnum = "123";
        for (Object x : cards
                ) {
            cardnum = (String) x;
            rs = con.getResult("Select * from Trip where BreezecardNum = '" + cardnum + "' and EndsAt is NULL");
            try {
                if (rs.next()) {//intrip
                    intrip = true;
                    cb.setDisable(false);
                    cb.setValue(cardnum);
                    cb.setDisable(true);
                    ResultSet rs1 = con.getResult("Select Value from Breezecard where BreezecardNum = " + "'" + cardnum + "'");
                    try {
                        while (rs1.next()) {
                            Balancenum.setText("$" + rs1.getString("Value".trim()));
                        }

                    } catch (Exception f) {
                        System.err.println("Exception5: " + f.getMessage());
                    }
                    System.out.println("In Trip Card:" + cardnum);
                    break;
                }
            } catch (Exception f) {
                System.err.println("Exception6: " + f.getMessage());
            }
        }
        ArrayList endstation = new ArrayList();
        if (intrip) {
            //get start station type
            rs = con.getResult("Select StartsAt from Trip where (BreezecardNum='" + cardnum + "'And EndsAt IS NULL)");
            try {
                rs.next();

                //filter for BUS or TRAIN
                String id=rs.getString("StartsAt".trim());
                rs=con.getResult("Select IsTrain from Station where StopID='"+id+"'");
                try{
                    rs.next();
                    if (rs.getString("IsTrain".trim()).contains("0")) {
                        for (Object s : stationname
                                ) {
                            String x = (String) s;
                            if (x.contains("BUS")) {
                                endstation.add(x);
                            }
                        }
                    } else {
                        for (Object s : stationname
                                ) {
                            String x = (String) s;
                            if (!x.contains("BUS")) {
                                endstation.add(x);
                            }
                        }
                    }
                }catch (Exception f) {
                    System.err.println("Exception71: " + f.getMessage());
                }
            } catch (Exception f) {
                System.err.println("Exception7: " + f.getMessage());
            }
            //in trip show end station
            cb2.setDisable(false);
            cb2.setItems(FXCollections.observableArrayList(endstation));
            endtrip.setDisable(false);
            endtrip.setVisible(true);
            alreadytrip.setVisible(true);
            Balancenum.setVisible(false);
        } else {
            //not in trip
            cb1.setDisable(false);
            cb.setDisable(false);
            cb2.setDisable(true);
            alreadytrip.setVisible(false);
            starttrip.setDisable(false);
            starttrip.setVisible(true);
            Balancenum.setVisible(true);
        }

        con.close();
        return;
    }


    public void endatrip() {
        List endstation = cb2.getItems();
        if(cb.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please choose Breeze Card");
            alert.showAndWait();
            return;
        }
        String cardnum1 = cb.getValue().toString();
        if(cb2.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please choose end station");
            alert.showAndWait();
            return;
        }
        String SelectEnd = cb2.getValue().toString();

        System.out.println(SelectEnd);
        String endid = "ENDID";
        final String end;
        ConnectionConfig con = new ConnectionConfig();
        if (SelectEnd.contains("BUS")) {
            String endchoice = SelectEnd.replace("-BUS", " ").trim();
            ResultSet rs1 = con.getResult("Select StopID from Station where (Name='" + endchoice + "' and IsTrain = FALSE)");
            try {
                rs1.next();
                endid = rs1.getString("StopID");
            } catch (Exception f) {
                System.err.println("Exception8: " + f.getMessage());
            }
        } else {
            ResultSet rs1 = con.getResult("Select StopID from Station where (Name='" + SelectEnd + "' and IsTrain = TRUE)");
            try {
                rs1.next();
                endid = rs1.getString("StopID");
            } catch (Exception f) {
                System.err.println("Exception9: " + f.getMessage());
            }
        }

        end = endid.trim();
        System.out.println(end);
        try {
            con.update("UPDATE Trip SET EndsAt = '" + end + "' Where(BreezecardNum='" + cardnum1 + "' AND EndsAt IS NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cb.setDisable(false);
        cb1.setDisable(false);
        cb2.setDisable(true);
        alreadytrip.setVisible(false);
        starttrip.setVisible(true);
        starttrip.setDisable(false);

        con.close();
        refresh();
    }

    public void cbclick() {
        final List cards = cb.getItems();
        //String cardsnum=cb.getValue().toString();
        //System.out.println(cardsnum);
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                String cardnum = (String) cards.get(newValue.intValue());
                ;
                System.out.println(cardnum);
                ConnectionConfig con = new ConnectionConfig();
                ResultSet rs1 = con.getResult("Select Value from Breezecard where BreezecardNum = " + "'" + cardnum + "'");
                try {
                    if(!rs1.next()){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Card not found");
                        alert.showAndWait();
                        return;
                    }else{
                        rs1.beforeFirst();
                    }
                    while (rs1.next()) {
                        Balancenum.setText("$" + rs1.getString("Value".trim()));
                    }
                } catch (Exception f) {
                    System.err.println("Exception10: " + f.getMessage());
                }
                con.close();
            }
        });
    }

    public void startatrip() {
        List station = cb1.getItems();
        if(cb.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please choose Breeze Card");
            alert.showAndWait();
            return;
        }
        String cards = cb.getValue().toString();
        System.out.println("Start with:" + cards);
        ConnectionConfig con = new ConnectionConfig();

        if(cb1.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please choose Start station");
            alert.showAndWait();
            return;
        }
        String SelectStart = cb1.getValue().toString();
        System.out.println(SelectStart);
        String[] splitstart = SelectStart.split("\\$");
        String startname = splitstart[0].trim();
        String Istrain;
        double credit = -1;
        double fare = -1;
        String newstartname;
        if (startname.contains("BUS")) {
            Istrain = "FALSE";
            newstartname=startname.replace("-BUS-", " ").trim();
            fare = Double.parseDouble(splitstart[1].trim());
        } else {
            Istrain = "TRUE";
             newstartname = startname.substring(0, startname.length() - 1);
            fare = Double.parseDouble(splitstart[1].trim());
        }

        credit = Double.parseDouble(Balancenum.getText().replace("$", " ").trim());
        double extra = -1;
        extra = credit - fare;
        System.out.println(newstartname);
        System.out.println("Fare:" + fare);
        System.out.println("Extra:" + extra);

        if (extra >= 0) {
            String farestr = String.valueOf(fare);
            String extrastr = String.valueOf(extra);
            String startid = "XXX";
            final String start;
            ResultSet rs7 = con.getResult("Select StopID from Station where (Name='" + newstartname + "' and IsTrain =" + Istrain + ")");
            try {
                rs7.next();
                startid = rs7.getString("StopID");
            } catch (Exception f) {
                System.err.println("Exception11: " + f.getMessage());
            }
            start = startid;
            System.out.println(start);

            try {
                con.update("INSERT INTO Trip (Tripfare,BreezecardNum,StartsAt) Values(" + farestr + ",'" + cards + "','" + start + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.update("UPDATE Breezecard SET Value =" + extrastr + " Where BreezecardNum ='" + cards + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            con.close();
            starttrip.setVisible(false);
            starttrip.setDisable(true);
            cb1.setDisable(true);
            cb.setDisable(true);
            cb2.setDisable(false);
            alreadytrip.setVisible(true);
            endtrip.setDisable(false);
            endtrip.setVisible(true);
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Insufficient Balance");
            alert.showAndWait();
            return;
        }
        refresh();

    }

    private void refresh(){
        boolean intrip = false;
        List cards=cb.getItems();
        List stationprice=cb1.getItems();
        ArrayList stationname=new ArrayList();
        for (Object x:stationprice
             ) {
            String s=(String) x;
            String[] splits=s.split("\\$");
            String name=splits[0].substring(0,splits[0].length()-1);
            stationname.add(name);
        }
        ConnectionConfig con = new ConnectionConfig();
        String cardnum = "123";
        for (Object x : cards
                ) {
            cardnum = (String) x;
            ResultSet rs = con.getResult("Select * from Trip where BreezecardNum = '" + cardnum + "' and EndsAt is NULL");
            try {
                if (rs.next()) {//intrip
                    intrip = true;
                    cb.setDisable(false);
                    cb.setValue(cardnum);
                    cb.setDisable(true);
                    ResultSet rs1 = con.getResult("Select Value from Breezecard where BreezecardNum = " + "'" + cardnum + "'");
                    try {
                        while (rs1.next()) {
                            Balancenum.setText("$" + rs1.getString("Value".trim()));
                        }

                    } catch (Exception f) {
                        System.err.println("Exception5: " + f.getMessage());
                    }
                    System.out.println("In Trip Card:" + cardnum);
                    break;
                }
            } catch (Exception f) {
                System.err.println("Exception6: " + f.getMessage());
            }
        }
        ArrayList endstation = new ArrayList();
        cb2.valueProperty().set(null);
        if (intrip) {
            //get start station type
            ResultSet rs = con.getResult("Select StartsAt from Trip where (BreezecardNum='" + cardnum + "'And EndsAt IS NULL)");
            try {
                rs.next();

                //filter for BUS or TRAIN
                String id=rs.getString("StartsAt".trim());
                rs=con.getResult("Select IsTrain from Station where StopID='"+id+"'");
                try{
                    rs.next();
                    if (rs.getString("IsTrain".trim()).contains("0")) {
                        for (Object s : stationname
                                ) {
                            String x = (String) s;
                            if (x.contains("BUS")) {
                                endstation.add(x);
                            }
                        }
                    } else {
                        for (Object s : stationname
                                ) {
                            String x = (String) s;
                            if (!x.contains("BUS")) {
                                endstation.add(x);
                            }
                        }
                    }
                }catch (Exception f) {
                    System.err.println("Exception7: " + f.getMessage()); }

            } catch (Exception f) {
                System.err.println("Exception7: " + f.getMessage());
            }
            //show end station
            cb2.setDisable(false);
            cb2.setItems(FXCollections.observableArrayList(endstation));
            endtrip.setDisable(false);
            endtrip.setVisible(true);
            alreadytrip.setVisible(true);
            Balancenum.setVisible(false);
        } else {
            cb1.setDisable(false);
            cb.setDisable(false);
            cb2.setDisable(true);
            alreadytrip.setVisible(false);
            starttrip.setDisable(false);
            starttrip.setVisible(true);
            Balancenum.setVisible(true);
        }

        con.close();
    }

    public void logout(){
        window.close();
    }

    public void managecards(){
        ManageCards.display(Tname);
    }

    public void viewhistory(){TripHistory.display(Tname);}

}

