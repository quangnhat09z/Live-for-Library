package com.example.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class MainController {
    @FXML
    private TextArea speech;
    @FXML
    private Label label1;
    @FXML
    private Button mainButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button setting_button;
    @FXML
    private Button user_button;

    @FXML
    public void initialize() {
        setSpeech("src/main/resources/book.txt");
        mainButton.setOnAction(actionEvent -> handleMainButton());
        updateButton.setOnAction(actionEvent -> handleUpdateButton());
        button2.setOnAction(actionEvent -> handleButton2());
        button3.setOnAction(actionEvent -> handleButton3());
        button4.setOnAction(actionEvent -> handleButton4());
        setting_button.setOnAction(actionEvent -> handleSettingButton());
        user_button.setOnAction(actionEvent -> handleUserButton());
    }

    private void handleMainButton() {
        // Xử lý cho nút Home
        System.out.println("Home button clicked");
    }

    private void handleUpdateButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/management-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void handleSettingButton() {
        //Xử lý cho nút setting
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/SettingsView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("SettingButton clicked");
    }

    private void handleUserButton() {
        System.out.println("UserButton clicked");
    }

    private void setLabel1(String s) {
        label1.setText(s);
    }

    private void setSpeech(String filePath) {
        try {
            String randomQuote = null;
            List<String> quotes = Files.readAllLines(Paths.get(filePath));
            while (randomQuote == null) {
                Random random = new Random();
                int randomIndex = random.nextInt(quotes.size());
                randomQuote = quotes.get(randomIndex);
            }
            System.out.println(randomQuote);
            speech.setText(randomQuote);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
