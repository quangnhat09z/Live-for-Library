package com.example.library.controller;

import com.example.library.model.DatabaseHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainController {
    public static final boolean ADMIN = true;
    @FXML
    private HBox root;
    @FXML
    private TextArea speech;
    @FXML
    private Label bookCount;
    @FXML
    private Button mainButton;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button admin_button;
    @FXML
    private Button setting_button;
    @FXML
    private Button user_button;

    @FXML
    public void initialize() {
        setSpeech("src/main/resources/quote.txt");
        mainButton.setOnAction(actionEvent -> handleMainButton());
        button1.setOnAction(actionEvent -> handleButton1());
        button2.setOnAction(actionEvent -> handleButton2());
        button3.setOnAction(actionEvent -> handleButton3());
        button4.setOnAction(actionEvent -> handleButton4());
        if (!ADMIN) {
            admin_button.setVisible(false);
        } else {
            admin_button.setOnAction(actionEvent -> handleAdminButton());
        }
        setting_button.setOnAction(actionEvent -> handleSettingButton());
        user_button.setOnAction(actionEvent -> handleUserButton());
        setBookCount();

        if (SettingsController.isDarkMode()) {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/dark_main.css")).toExternalForm());

        } else {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/main.css")).toExternalForm());

        }
    }

    private void handleMainButton() {
        // Xử lý cho nút Home
        System.out.println("Home button clicked");
    }

    private void handleButton1() {
        // Xử lý cho nút Button1
        System.out.println("Button1 clicked");
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

    private void handleAdminButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/update-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) button1.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSettingButton() {
        //Xử lý cho nút setting
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/settings-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) button1.getScene().getWindow();
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


    private void setBookCount() {

        String query = "SELECT COUNT(*) AS count FROM documents";

        try (Connection conn = DriverManager.getConnection(DatabaseHelper.URL, DatabaseHelper.USER, DatabaseHelper.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int count = rs.getInt("count");
                bookCount.setText(String.valueOf(count));
            }

        } catch (Exception e) {
            e.printStackTrace();
            bookCount.setText("");
            System.out.println("Không thể lấy số lượng sách");
        }
    }
}
