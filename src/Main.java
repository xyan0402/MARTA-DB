import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Main extends Application {
    Button button;
    Button button2;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Marta Station");
        button = new Button("Log in");
        button2= new Button("Register");

        try {

            button.setOnAction(e -> {
                try {
                    Login.display();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            button2.setOnAction(e -> {
                try {
                    CreateAccount.display();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.getMessage();
        }



        VBox layout = new VBox(10);
        layout.getChildren().addAll(button,button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    @Override
//    public void handle(ActionEvent event) {
//        if(event.getSource()== button) {
//            System.out.println("Hello World");
//        }
//    }
}
