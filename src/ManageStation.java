
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageStation {
    @FXML
    private TableView tableView;

    @FXML
    public TableColumn<Station, String> colstationName;

    @FXML
    public TableColumn<Station, String> colstopID;

    @FXML
    public TableColumn<Station, String> colfare;

    @FXML
    public TableColumn<Station, String> colstatus;


    private ObservableList<Station> data;

    public static void display()throws Exception{
        Parent root = FXMLLoader.load(ManageStation.class.getResource("ManageStation.fxml"));


        Stage window = new Stage();

        Scene scene = new Scene(root, 600, 500);

        window.setTitle("Manage Station");
        window.setScene(scene);
        window.show();
    }


    public void createStationOnAction(ActionEvent actionEvent) {
        try {
            CreateStation.display();

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public void viewStationOnAction(ActionEvent actionEvent) {
        try {
            Station sta = (Station)tableView.getSelectionModel().getSelectedItem();
            System.out.println(sta.getStopID());
            String stop = sta.getStopID();
            ViewStation.display(stop);

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public void seeStation(ActionEvent actionEvent) {
        ConnectionConfig con = new ConnectionConfig();
        String sql = "select * from Station";
        ResultSet rs = con.getResult(sql);
        //ResultSet rs2 = con.getResult("select BreezecardNum from Conflict");
        //String breezenum;
        data = FXCollections.observableArrayList();
        try {
            while(rs.next()){


                data.add(new Station(rs.getString("Name"), rs.getString("StopID"), rs.getString("EnterFare"), rs.getString("ClosedStatus")));



            }

            colstationName.setCellValueFactory(new PropertyValueFactory<>("stationName"));
            colstopID.setCellValueFactory(new PropertyValueFactory<>("stopID"));
            colfare.setCellValueFactory(new PropertyValueFactory<>("Fare"));
            colstatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

            tableView.setItems(null);

            tableView.setItems(data);
        } catch (SQLException e) {
            System.out.println("sql exception");
            e.printStackTrace();
        }
        con.close();


    }
}
