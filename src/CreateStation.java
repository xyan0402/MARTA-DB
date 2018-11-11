import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;


import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateStation {

    @FXML
    public TextField stationName;
    @FXML
    public TextField StopID;
    @FXML
    public TextField EntryFare;
    @FXML
    public TextField NearestIntersection;
    @FXML
    public RadioButton BusStationChecked;
    @FXML
    public RadioButton TrainStationChecked;

    @FXML
    private RadioButton OpenStationChecked;

    private static Stage Window;

    public static void display() throws Exception {
        Parent root = null;
        try {
            System.out.println("print");
            root = FXMLLoader.load(CreateStation.class.getResource("CreateStation.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage window = new Stage();

        Scene scene = new Scene(root, 600, 500);



        window.setTitle("Create Station");
        window.setScene(scene);
        window.show();

    }


    // If the stop is a bus station
    public void BusStationSelected(ActionEvent actionEvent) {
        NearestIntersection.setEditable(BusStationChecked.isSelected());
        TrainStationChecked.setDisable(BusStationChecked.isSelected());
    }

    // If the stop is a train station, disable the NearestIntersection field
    public void TrainStationSelected(ActionEvent actionEvent) {
        BusStationChecked.setDisable(TrainStationChecked.isSelected());
    }


    public void CreateStation(ActionEvent actionEvent) {
        String name = stationName.getText();
        System.out.println("name %s" + name);
        String ID = StopID.getText();
        System.out.println("ID %s" + ID);
        double fare = Double.parseDouble(EntryFare.getText());
        System.out.println("fare %s" + fare);
        String intersec = NearestIntersection.getText();
        System.out.println("intersec %s" + intersec);
//        // If is train station, bool(isTrain = 1)
        String bool = TrainStationChecked.isSelected() == true? "1":"0";
        System.out.println(bool);
//        // If is an open station, IsClosed = 0
        String bool2 = OpenStationChecked.isSelected() == true? "0":"1";
        System.out.println(bool2);
//
       ConnectionConfig con = new ConnectionConfig();

        // Check if the stop ID is unique
        ResultSet rs = con.getResult("Select StopID from Station where StopID = " + "'" + ID + "'");
        System.out.println("Select StopID from Station where StopID = " + "'" + ID + "'");


        try {
            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Stop ID already exists!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Check if the entry fare is between $0 to $50
            if (fare < 0 || fare > 50) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Fare is out of range!");
                alert.showAndWait();
            }
            // Insert data into database
            else {
                System.out.println("Insert into Station Values ('" + ID + "','" + name + "','" + fare + "','" + bool2 + "','" + bool + "')");
                try {
                    con.update("Insert into Station Values ('" + ID + "','" + name + "','" + fare + "','" + bool2 + "','" + bool + "')");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //con.update("Insert into Station Values ('" + ID + "','" + name + "','" + fare + "','" + bool2 + "','" + bool + "')");

                // If is a bus station
                if (bool == "0") {
                    try {
                        con.update("Insert into BusStationIntersection Values ('" + ID + "','" + intersec + "')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Insert into BusStationIntersection Values ('" + ID + "','" + intersec + "')");
                }

            }



        con.close();
    }

    public void check(ActionEvent actionEvent) {
    }

}


