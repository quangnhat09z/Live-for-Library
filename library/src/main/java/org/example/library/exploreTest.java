package org.example.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class exploreTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Tải file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("explore-view.fxml"));
        Parent root = loader.load();

        // Thiết lập Scene và Stage
        primaryStage.setTitle("Explore");
        primaryStage.setScene(new Scene(root, 1100, 650)); // Kích thước cửa sổ
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}