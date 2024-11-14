package com.example.library.controller;

import com.example.library.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateController {
    @FXML
    private Button homeButton;
    @FXML
    private Button mainButton;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;

    @FXML
    public void initialize() {
        homeButton.setOnAction(actionEvent -> handleHomeButton());
        mainButton.setOnAction(actionEvent -> handleMainButton());
        button2.setOnAction(actionEvent -> handleButton2());
        button3.setOnAction(actionEvent -> handleButton3());
        button4.setOnAction(actionEvent -> handleButton4());
    }

    private void handleHomeButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMainButton() {
        // Xử lý cho nút Main (Update)
    }

    private void handleButton2() {
        // Xử lý cho nút Button2
        System.out.println("Button2 clicked");
    }

    private void handleButton3() {
        // Xử lý cho nút Button3
        System.out.println("Button3 clicked");
    }

    private void handleButton4() {
        // Xử lý cho nút Button4
        System.out.println("Button4 clicked");
    }


}
