package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            MusicLibrary.loadAll();

            Parent root = FXMLLoader.load(getClass().getResource("Entry.fxml"));
            primaryStage.setScene(new Scene(root, 370, 480));
            primaryStage.setResizable(false);
            primaryStage.setTitle("Echo");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}