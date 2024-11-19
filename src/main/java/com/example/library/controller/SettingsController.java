package com.example.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SettingsController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox root;
    @FXML
    private Button homeButton;
    @FXML
    private Button button1;
    @FXML
    private Button exploreButton;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button saveButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private CheckBox darkModeCheckBox;
    @FXML
    private CheckBox notificationsCheckBox;

    private static String nameUser;
    private static String emailUser;
    private static boolean darkMode;
    private static boolean notification = true;

    public void initialize() {
        homeButton.setOnAction(actionEvent -> handleHomeButton());
        button1.setOnAction(actionEvent -> handleButton1());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        button3.setOnAction(actionEvent -> handleButton3());
        button4.setOnAction(actionEvent -> handleButton4());
        saveButton.setOnAction(actionEvent -> handleSaveButton());

        nameField.setText(nameUser);
        emailField.setText(emailUser);
        darkModeCheckBox.setSelected(darkMode);
        notificationsCheckBox.setSelected(notification);

        if (darkMode) {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/dark_settings.css")).toExternalForm());

            scrollPane.getStylesheets().clear();
            scrollPane.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/dark_settings.css")).toExternalForm());

        } else {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/settings.css")).toExternalForm());

            scrollPane.getStylesheets().clear();
            scrollPane.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/settings.css")).toExternalForm());
        }
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    private void handleHomeButton() {
        changeScene("/com/example/library/main-view.fxml", "Main");
    }

    private void handleButton1() {
        // Xử lý cho nút Button1
        System.out.println("Button1 clicked");
    }

    private void handleExploreButton() {
        // Xử lý cho nút Button2
        changeScene("/com/example/library/explore-view.fxml", "Explore");
    }

    private void handleButton3() {
        // Xử lý cho nút Button3
        System.out.println("Button3 clicked");
    }

    private void handleButton4() {
        // Xử lý cho nút Button4
        System.out.println("Button4 clicked");
    }

    private void handleSaveButton() {
        String name = nameField.getText();
        String email = emailField.getText();
        boolean darkMode = darkModeCheckBox.isSelected();
        boolean notifications = notificationsCheckBox.isSelected();

        nameUser = name;
        emailUser = email;
        SettingsController.darkMode = darkMode;
        notification = notifications;
        initialize();
    }

    private void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button1.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
