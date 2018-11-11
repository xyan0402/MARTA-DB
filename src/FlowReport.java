/*
Create a Passenger Flow Report page
*/

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.io.IOException;
import java.security.*;
import javax.swing.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.*;

@SuppressWarnings("unused")
public class FlowReport {

    @FXML
    public TextField StartTime;
    @FXML
    public TextField EndTime;
    @FXML
    public Button update;
    @FXML
    public Button reset;
    @FXML
    public TableView<Record> report;
    @FXML
    public TableColumn<Record, String> columnStationName;
    @FXML
    public TableColumn<Record, String> columnPassengerIn;
    @FXML
    public TableColumn<Record, String> columnPassengerOut;
    @FXML
    public TableColumn<Record, String> columnFlow;
    @FXML
    public TableColumn<Record, String> columnRevenue;

    public ObservableList<Record> data;

    private static Stage Window;

    ResultSet rs;

    public static void display() {
        Parent root = null;
        try {
            root = FXMLLoader.load(FlowReport.class.getResource("FlowReport.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage window = new Stage();

        Scene scene = new Scene(root, 800,600);

        window.setTitle("Passenger Flow Report");
        window.setScene(scene);
        window.show();

    }

    // If click on "Update" button, show the passenger flow report
    public void update(ActionEvent actionEvent) {

        String startTime = StartTime.getText();
        String endTime = EndTime.getText();
        // If there is no input for start time and end time
        // Show all the records
        if (startTime.length()== 0 && endTime.length()== 0) {
            System.out.println("hello");
            ConnectionConfig con = new ConnectionConfig();
            String sql1 = "(SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "

                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL LEFT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL) "
                    + "UNION(" +
                    "SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "

                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL RIGHT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL)";
            rs = con.getResult(sql1);

            data = FXCollections.observableArrayList();


        }
        // If start time is defined
        else if (startTime.length()!= 0 && endTime.length() == 0){
            System.out.println("hello2");
            ConnectionConfig con = new ConnectionConfig();
            String sql1 = "(SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "
                    + "WHERE StartTime>='" + startTime + "' AND StartTime<='" + endTime + "' "
                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL LEFT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + " WHERE StartTime>=' " + startTime + "'  "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL) "
                    + "UNION(" +
                    "SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "
                    + "WHERE StartTime>='" + startTime + "' "
                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL RIGHT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + "WHERE StartTime>= '" + startTime + " ' "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL)";
            System.out.println(sql1);
            rs = con.getResult(sql1);

            data = FXCollections.observableArrayList();


        }
        // If end time is defined
        else if  (startTime.length() == 0 && endTime.length() != 0) {
            ConnectionConfig con = new ConnectionConfig();
            String sql1 = "(SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "
                    + "WHERE  StartTime<='" + endTime + "' "
                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL LEFT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + " WHERE  StartTime<='" + endTime + "' "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL) "
                    + "UNION(" +
                    "SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "
                    + "WHERE  StartTime<='" + endTime + "'"
                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL RIGHT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + "WHERE StartTime<='" + endTime + "' "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL)";
            rs = con.getResult(sql1);

            data = FXCollections.observableArrayList();

        }
        // If start time and end time are all defined
        else{

//        // Check if the input format is correct
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY HH");
//        dateFormat.setLenient(false);
//        try {
//            dateFormat.parse(startTime);
//        } catch (ParseException pe) {
//            return false;
//        }
//        return true;
            ConnectionConfig con = new ConnectionConfig();
            String sql1 = "(SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "
                    + "WHERE StartTime>='" + startTime + "' AND StartTime<='" + endTime + "' "
                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL LEFT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + " WHERE StartTime>=' " + startTime + "' AND StartTime<='" + endTime + "' "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL) "
                    + "UNION(" +
                    "SELECT Name, PassengerIn, PassengerOut, Revenue "
                    + "FROM (SELECT StopID,Name, COUNT(*) AS PassengerIn,Sum(TripFare) AS Revenue "
                    + "FROM Trip JOIN Station ON Trip.StartsAt=Station.StopID "
                    + "WHERE StartTime>='" + startTime + "' AND StartTime<='" + endTime + "'"
                    + " GROUP BY StartsAt) AS S "
                    + "NATURAL RIGHT OUTER JOIN "
                    + "(SELECT StopID,Name, COUNT(*) AS PassengerOut "
                    + "FROM Trip JOIN Station ON Trip.EndsAt=Station.StopID "
                    + "WHERE StartTime>= '" + startTime + "'AND StartTime<='" + endTime + "' "
                    + "GROUP BY EndsAt) AS F "
                    + "WHERE Name IS NOT NULL)";
            rs = con.getResult(sql1);

            data = FXCollections.observableArrayList();
        }

            try {
                while (rs.next()) {
                    String temp1 = rs.getString("PassengerIn");
                    String temp2 = rs.getString("PassengerOut");
                    String temp3 = rs.getString("Revenue");

                    if (temp1 == null) {
                        temp1 = "0";
                    }
                    if (temp2 == null) {
                        temp2 = "0";
                    }
                    if (temp3 == null) {
                        temp3 = "0.00";
                    }
                    int t1 = Integer.parseInt(temp1);
                    int t2 = Integer.parseInt(temp2);
                    String result = Integer.toString(t1 - t2);
                    data.add(new Record(rs.getString("Name"), temp1, temp2, result, temp3));

                    columnStationName.setCellValueFactory(new PropertyValueFactory<>("Name"));
                    columnPassengerIn.setCellValueFactory(new PropertyValueFactory<>("PassengerIn"));
                    columnPassengerOut.setCellValueFactory(new PropertyValueFactory<>("PassengerOut"));
                    columnFlow.setCellValueFactory(new PropertyValueFactory<>("res"));
                    columnRevenue.setCellValueFactory(new PropertyValueFactory<>("Revenue"));


                    report.setItems(null);

                    report.setItems(data);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }


    // If click on "Reset" button, clear all the contents in this page
    public void reset(ActionEvent actionEvent) {
        StartTime.clear();
        report.setItems(null);
        EndTime.clear();
    }

}
