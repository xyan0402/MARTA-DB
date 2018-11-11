
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


public class TripHistory {
    @FXML
    private TextField starttime;
    @FXML
    private TextField endtime;
    @FXML
    private Button update;
    @FXML
    private Button reset;
    @FXML
    private TableView<trip> triplist;
    @FXML
    private TableColumn<trip,String> timecol;
    @FXML
    private TableColumn<trip,String> startatcol;
    @FXML
    private TableColumn<trip,String> endatcol;
    @FXML
    private TableColumn<trip,String> farecol;
    @FXML
    private TableColumn<trip,String> breezecol;

    public ObservableList<trip> data;
    public ArrayList stationname;

    private static Stage window;
    private static String Tname;
    public static void display(String name){
        try {
            Tname = name;
            Parent root = FXMLLoader.load(TripHistory.class.getResource("TripHistory.fxml"));

            window = new Stage();
            Scene scene = new Scene(root);
            window.setTitle("Manage Cards");
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            System.out.println("Exception1:" + e.getMessage());
        }
    }

    public void update(){
        {
            String startt=starttime.getText().trim();
            String endt=endtime.getText().trim();
            ConnectionConfig con=new ConnectionConfig();
            String SQL="";
            if(startt.equals("")){
                if (endt.equals("")){
                    SQL="SELECT * FROM Trip WHERE EXISTS ( SELECT * FROM Breezecard WHERE (Trip.BreezecardNum = Breezecard.BreezecardNum) AND (Breezecard.BelongsTo ='"+Tname+"') AND NOT EXISTS (SELECT BreezecardNum FROM Conflict WHERE BreezecardNum = Breezecard.BreezecardNum ))";
                }else{
                    SQL="SELECT * FROM Trip WHERE (Trip.StartTime<'"+endt+"') AND EXISTS ( SELECT * FROM Breezecard WHERE (Trip.BreezecardNum = Breezecard.BreezecardNum) AND (Breezecard.BelongsTo ='"+Tname+"') AND NOT EXISTS (SELECT BreezecardNum FROM Conflict WHERE BreezecardNum = Breezecard.BreezecardNum ))";
                }
            }else {
                if(endt.equals("")){
                    SQL="SELECT * FROM Trip WHERE (Trip.StartTime>'"+startt+"') AND EXISTS ( SELECT * FROM Breezecard WHERE (Trip.BreezecardNum = Breezecard.BreezecardNum) AND (Breezecard.BelongsTo ='"+Tname+"') AND NOT EXISTS (SELECT BreezecardNum FROM Conflict WHERE BreezecardNum = Breezecard.BreezecardNum ))";
                }else{
                    SQL="SELECT * FROM Trip WHERE (Trip.StartTime<'"+endt+"')AND(Trip.StartTime>'"+startt+"') AND EXISTS ( SELECT * FROM Breezecard WHERE (Trip.BreezecardNum = Breezecard.BreezecardNum) AND (Breezecard.BelongsTo ='"+Tname+"') AND NOT EXISTS (SELECT BreezecardNum FROM Conflict WHERE BreezecardNum = Breezecard.BreezecardNum ))";
                }
            }
            //SQL="SELECT * FROM Trip WHERE EXISTS ( SELECT * FROM Breezecard WHERE (Trip.BreezecardNum = Breezecard.BreezecardNum) AND (Breezecard.BelongsTo ='"+Tname+"') AND NOT EXISTS (SELECT BreezecardNum FROM Conflict WHERE BreezecardNum = Breezecard.BreezecardNum ))";

            ResultSet rs = con.getResult(SQL);
            data = FXCollections.observableArrayList();
            try {
                if(!rs.next()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("No trip history found");
                    alert.showAndWait();
                    return;
                }
                    rs.beforeFirst();
                while (rs.next()) {
                    String startatname="-1";
                    String endatname="-1";
                    String starttimes = rs.getString("StartTime").trim();
                    System.out.println(starttimes);
                    String startats=rs.getString("StartsAt").trim();
                    ResultSet rs1=con.getResult("Select Name, IsTrain from Station where StopID='"+startats+"'");
                    rs1.next();
                    if(rs1.getString("IsTrain").trim().contains("0")){
                        startatname=rs1.getString("Name").trim()+"-BUS";
                    }else {
                        startatname=rs1.getString("Name").trim();
                    }
                    String endats;
                    if (rs.getString("EndsAt")==null){
                        endatname="In Trip";
                        System.out.println("endsat = null");
                    }else{
                        endats=rs.getString("EndsAt").trim();
                        ResultSet rs2=con.getResult("Select Name, IsTrain from Station where StopID='"+endats+"'");
                        rs2.next();
                        if(rs2.getString("IsTrain").trim().contains("0")){
                            endatname=rs2.getString("Name").trim()+"-BUS";
                        }else {
                            endatname=rs2.getString("Name").trim();
                        }
                    }
                    String fares="$"+rs.getString("TripFare").trim();
                    String breezes=rs.getString("BreezecardNum").trim();
                    data.add(new trip(starttimes,startatname,endatname,fares,breezes));
                }

            } catch (Exception f) {
                System.err.println("Exception2: " + f.getMessage());
                //System.out.println("111");
            }
            con.close();
            timecol.setCellValueFactory(new PropertyValueFactory<>("starttime"));
            startatcol.setCellValueFactory(new PropertyValueFactory<>("startat"));
            endatcol.setCellValueFactory(new PropertyValueFactory<>("endat"));
            farecol.setCellValueFactory(new PropertyValueFactory<>("fare"));
            breezecol.setCellValueFactory(new PropertyValueFactory<>("breezenum"));
            triplist.setItems(null);
            triplist.setItems(data);
        }
    }

    public void reset(){
        triplist.setItems(null);
    }


}
