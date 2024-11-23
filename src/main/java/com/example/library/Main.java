package com.example.library;

import java.util.Objects;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
  //Login

//  @Override
//  public void start(Stage primaryStage) throws Exception {
//    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/library/login-login-view.fxml")));
//    primaryStage.setScene(new Scene(root, 520, 400));
//    primaryStage.initStyle(StageStyle.UNDECORATED);
//    primaryStage.setResizable(false);
//    primaryStage.show();
//  }


     @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/library/search-accounts-view.fxml")));
        primaryStage.setTitle("Live for Library");
        primaryStage.setScene(new Scene(root, 1300, 650));
        primaryStage.setResizable(false);
        primaryStage.show();
    }





  public static void main(String[] args) {
    launch(args);
  }
}
