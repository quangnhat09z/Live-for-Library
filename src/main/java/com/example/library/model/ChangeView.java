package com.example.library.model;

import com.example.library.controller.SettingsController;
import javafx.animation.PauseTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Set;

public class ChangeView {

    public static void changeViewFXML(String url, Stage stage) {
        // Wait 1.5s for change
        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
        pause.setOnFinished(event -> {
            try {
                // Upload
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ChangeView.class.getResource(url)));
                Parent root = loader.load();
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