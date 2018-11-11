import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewStation {
    @FXML
    private Label StationName;

    @FXML
    private TextField Fare;

    @FXML
    private TextField NearestIntersection;

    @FXML
    private CheckBox OpenStationChecked;

    private static String sID;

    public static void display(String stopID) throws Exception {
        Parent root = FXMLLoader.load(ManageStation.class.getResource("ViewStation.fxml"));
        sID = stopID;

        Stage window = new Stage();

        Scene scene = new Scene(root, 600, 500);

        window.setTitle("View Station");
        window.setScene(scene);
        window.show();
    }
    public void Update(ActionEvent actionEvent) {
        double newFare = Double.parseDouble(Fare.getText());
        String bool = OpenStationChecked.isSelected() == true? "1":"0";



        if (newFare < 0 || newFare > 50) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Fare is out of range");
            alert.showAndWait();
        } else {
            String sql = "Update Station Set EnterFare =  '" + newFare + "', ClosedStatus = '" + bool + "' Where StopID = '" + sID + "'";
            System.out.println(sql);
            ConnectionConfig con = new ConnectionConfig();
            try {
                con.update(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            con.close();


        }
    }
    public void refresh(ActionEvent actionEvent) {
        String sql = "SELECT * FROM Station NATURAL LEFT OUTER JOIN BusStationIntersection where StopID = '"+sID + "'";
        String sql2 = "Select * from Station where StopID = '" + sID + "'";
        ConnectionConfig con = new ConnectionConfig();
        ResultSet rs = con.getResult(sql);
        ResultSet rs2 = con.getResult(sql2);

        try {
            rs.next();
            rs2.next();
            String Intersection="";
            if(rs.getString("Intersection")!=null) {
                Intersection = rs.getString("Intersection");
            }
            String name = rs2.getString("Name");
            String fare = rs2.getString("EnterFare");
            String type = rs2.getString("ClosedStatus");
            NearestIntersection.setText(Intersection);
            Fare.setText(fare);
            StationName.setText(name);
            if (type.trim().equals("0")) {
                OpenStationChecked.setSelected(true);

            }
            con.close();



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
