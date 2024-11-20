package com.example.library.model;

import javafx.animation.PauseTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;

public class ChangeView {

  public static void changeViewFXML(String url, Stage stage) {
    // Wait 1.5s for change
    PauseTransition pause = new PauseTransition(Duration.seconds(1.5)); // Đợi 2 giây
    pause.setOnFinished(event -> {
      try {
        // Upload
        Parent root = FXMLLoader.load(ChangeView.class.getResource(url));
        stage.setTitle("Live for Library");
        stage.setScene(new Scene(root, 1300, 650));
        stage.setResizable(false);

        // Set position
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        // center position
        stage.setX((screenWidth - stageWidth) / 2);
        stage.setY((screenHeight - stageHeight) / 2);
        stage.show();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    pause.play();
  }
}
