/*
Create a MARTA Account page
*/

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
// import sun.security.util.Password;

import java.security.*;
import javax.swing.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

public class CreateAccount {

    public static void display() {
        // Form of the Create Account stage
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create a MARTA Account");
        VBox layout1 = new VBox(10);

        Label labelName = new Label ("Username");
        TextField nameInput = new TextField();
        HBox hbName = new HBox(10);
        hbName.getChildren().addAll(labelName, nameInput);

        Label labelEmail = new Label ("Email Address");
        TextField emailInput = new TextField();
        HBox hbEmail = new HBox(10);
        hbEmail.getChildren().addAll(labelEmail, emailInput);

        Label labelPass = new Label ("Password");
        PasswordField password = new PasswordField();   // To evaluate password: password.getText
        password.setPromptText("Please Input Password");
        HBox hbPass = new HBox(10);
        hbPass.getChildren().addAll(labelPass, password);

        Label labelCpass = new Label ("Confirm Password");
        PasswordField CpassInput = new PasswordField();
        CpassInput.setPromptText("Please Confirm Password");
        HBox hbCpass = new HBox(10);
        hbCpass.getChildren().addAll(labelCpass, CpassInput);

        HBox labeledSeparator = new HBox();
        Label labelSep = new Label("Breeze Card");
        Separator leftSeparator = new Separator();
        Separator rightSeparator = new Separator();
        leftSeparator.setPrefWidth(200);
        rightSeparator.setPrefWidth(200);
        labeledSeparator.getChildren().addAll(leftSeparator, labelSep, rightSeparator);
        labeledSeparator.setAlignment(Pos.CENTER);

        layout1.getChildren().addAll(hbName, hbEmail, hbPass, hbCpass);  // Input boxes
        layout1.setSpacing(15);
        layout1.getChildren().add(labeledSeparator);   // Separator

        RadioButton rb1 = new RadioButton("Use my existing Breeze Card");
        layout1.getChildren().add(rb1);
        Label labelCard = new Label ("Card Number");
        TextField cardNumber = new TextField();
        HBox hbCard = new HBox(10);
        hbCard.getChildren().addAll(labelCard, cardNumber);
        layout1.getChildren().add(hbCard);
        hbCard.setAlignment(Pos.CENTER);
        RadioButton rb2 = new RadioButton("Get a new Breeze Card");
        layout1.getChildren().add(rb2);
        rb1.setAlignment(Pos.CENTER);
        rb2.setAlignment(Pos.CENTER);
        ToggleGroup group = new ToggleGroup();
        rb1.setToggleGroup(group);
        rb2.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (group.getSelectedToggle() == rb2) {
                    cardNumber.setDisable(true);
                }
                else {
                    cardNumber.setDisable(false);
                }
            }
        });

        Button button = new Button("Create Account");

        button.setOnAction((ActionEvent e) -> {
            String name = nameInput.getText();
            String email = emailInput.getText();
            String pass = password.getText();
            String cardNo = cardNumber.getText();

            // Set pattern for email address
            Pattern pattern;
            Matcher matcher;
            String EMAIL_PATTERN =
                    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);

            ConnectionConfig con = new ConnectionConfig();
            try {
                // Check if password is accordance with confirmed password
                if (!password.getText().trim().equals(CpassInput.getText().trim())) {
                    //System.out.println("Your password and confirm password don't match");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error Operation!");
                    alert.setContentText("Your password and confirm password don't match!");
                    alert.showAndWait();
                }
                // Check if the password is less than 8 characters
                else if (pass.length() < 8){
                    // System.out.println("Your password should exceed 8 characters");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error Operation!");
                    alert.setContentText("Your password should exceed 8 characters!");
                    alert.showAndWait();
                }
                // Check if the email address is in the right format
                else if (matcher.matches() == false) {
                    // System.out.println("Your email address is invalid");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error Operation!");
                    alert.setContentText("Your email address is invalid!");
                    alert.showAndWait();
                }
                else {
                    System.out.println("Enter into whole breezecard!");
                    // Insert data into User
                    MessageDigest m = MessageDigest.getInstance("MD5");
                    m.update(pass.getBytes(), 0, pass.length());
                    String myHash = String.format("%032x", new BigInteger(1, m.digest()));
                    try {  // If input duplicated usesrname and email, an alert window shows up
                        con.update("Insert into USER Values" + "(" + "'" + name + "'" + "," + "'" + myHash + "'" + "," + "'" + "0" + "'" + ")");
                        // Insert data into passenger
                        con.update("Insert into Passenger Values" + "('" + name + "','" + email + "')");
                    }
                    catch (SQLException g) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error Operation!");
                        alert.setContentText("Your Username and/or email address already exist!");
                        alert.showAndWait();
//                        System.out.println("Exception:" + g.getMessage());
                    }

                    // Event trigger based on selecting
                    // Get a new card Or Use existing card
                    if (group.getSelectedToggle() == rb2) {   // If "Get a new Breeze Card" is selected)
                        System.out.println("Here");
                        long cardNum = 0;  // Check if the generated card number exists
                        boolean IsDuplicated = true;
                        while (IsDuplicated) {
                            java.util.Random rng = new java.util.Random();
                            cardNum = (rng.nextLong() % 10000000000000000L);  // ***cardNum and strNum*** is for get a new card!!!!!!
                            String strNum = Long.toString(cardNum);    // Convert long to string
                            // cardNum = char(0123456780987654);
                            ResultSet rs = con.getResult("Select * from Breezecard where BreezecardNum = '" + strNum + "'");
                            if (!rs.next()) {   // The card number doesn't exist
                                IsDuplicated = false;
                            }
                        }
                        String strNum = Long.toString(cardNum);
                        con.update("Insert into Breezecard(BreezecardNum,BelongsTo) Values('" + strNum + "','" + name + "')");
                    }
                    else if(group.getSelectedToggle() == rb1) {   // If "Use Existing Card" is selected

                        if(cardNo.length() != 16) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error Operation!");
                            alert.setContentText("Your card number should be 16 digits!");
                            alert.showAndWait();
                        }
                        else {
                            ResultSet rs = con.getResult("Select BelongsTo from Breezecard where BreezecardNum = '" + cardNo + "'");
                            if (rs.next()) {
                                String test = rs.getString("BelongsTo");
                                System.out.println(rs.getString("BelongsTo"));
                                if ((rs.getString("BelongsTo")) == null) {  // If null, update
                                    System.out.println("Here2");
                                    con.update("Update Breezecard Set BelongsTo = '" + name + "'" + "where BreezecardNum = '" + cardNo + "'");
                                }
                                else if ((rs.getString("BelongsTo")) != null){  // If not null, insert conflict
                                    ResultSet time = con.getResult("Select Now()");
                                    if (time.next()) {
                                        String t = time.getString("NOW()");
                                        con.update("Insert into Conflict Values('" + name + "','" + cardNo + "','" + t + "')");
                                        // Randomly generate a card
                                        long cardNoo = 0;
                                        java.util.Random rng = new java.util.Random();
                                        cardNoo = (rng.nextLong() % 10000000000000000L);  // ***cardNum and strNum*** is for get a new card!!!!!!
                                        String strNum = Long.toString(cardNoo);    // Convert long to string
                                        con.update("Insert into Breezecard Values('" + cardNoo + "'," + 0.00 + ",'" + name + "')");

                                    }
                                }
                            }
                            else {   // If returns an empty set, insert breezecard
                                System.out.println("Insert into Breezecard Values('" + cardNo + "'," + 0.00 + "'" + name + "'");
                                con.update("Insert into Breezecard Values('" + cardNo + "'," + 0.00 + ",'" + name + "')");
                            }
                        }
                    }
                    else {   // If no button is selected
                        System.out.println("no radio button is selected!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error Operation!");
                        alert.setContentText("You must choose an option under breezecard!");
                        alert.showAndWait();
                    }
                }
            } catch (Exception f) {
                System.err.println("Exception: " + f.getMessage());
            }
            con.close();

        });


        layout1.setSpacing(15);
        layout1.getChildren().add(button);
        layout1.setAlignment(Pos.CENTER);

        hbName.setAlignment(Pos.CENTER);
        hbPass.setAlignment(Pos.CENTER);
        hbEmail.setAlignment(Pos.CENTER);
        hbCpass.setAlignment(Pos.CENTER);


        Scene scene1 = new Scene(layout1, 500, 800);

        window.setScene(scene1);
        window.showAndWait();

    }
}
