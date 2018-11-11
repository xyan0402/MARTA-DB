/*
Login page
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.ResultSet;



public class Login {

    @FXML
    private TextField Username;
    @FXML
    private TextField Password;
    @FXML
    private Button Login;

    private static Stage window;

    public static void display() throws Exception{
        Parent root = FXMLLoader.load(Login.class.getResource("Login.fxml"));
        window = new Stage();

        Scene scene = new Scene(root, 600, 500);

        window.setTitle("Login");
        window.setScene(scene);
        window.show();

    }

    public void loginOnAction(ActionEvent actionEvent) {
        String name = Username.getText();
        String pass = Password.getText();
        System.out.println(name);
        System.out.println(pass);
        ConnectionConfig con = new ConnectionConfig();
        System.out.println("Select Password , IsAdmin from USER where Username = " + "'" + name + "'");
        ResultSet rs = con.getResult("Select Password, IsAdmin from USER where Username = " + "'" + name + "'");
        try {
            if (!rs.next()) {
                System.out.println("The account does not exist ");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("The account does not exist");
                alert.showAndWait();
            } else {
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("I am here");
                    MessageDigest m = MessageDigest.getInstance("MD5");
                    m.update(pass.getBytes(), 0, pass.length());
                    String myHash = String.format("%032x", new BigInteger(1, m.digest()));

                    if (myHash.equals(rs.getString("Password").trim())) {
                        String isAdmin = rs.getString("IsAdmin").trim();
                        System.out.println(isAdmin.length()+"jere");
                        if (isAdmin.equals("1")) {
                            System.out.println("here");
                            Manager.display();
                            window.close();

                        } else {
                            Passenger2.display(name);
                        }
                    } else {
                        System.out.println("Np");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText("Incorrect UserName / Password");
                        alert.showAndWait();
                    }
                }
            }
        } catch (Exception f) {
            System.err.println("Exception: " + f.getMessage());

        }
        con.close();

    }

}
