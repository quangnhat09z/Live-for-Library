package com.example.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    @FXML
    private Pane pane;
    @FXML
    private HBox root;
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
    private static boolean isDarkMode;
    private static boolean isNotification = true;

    public void initialize() {
        mainButton.setOnAction(actionEvent -> handleMainButton());
        updateButton.setOnAction(actionEvent -> handleUpdateButton());
        button2.setOnAction(actionEvent -> handleButton2());
        button3.setOnAction(actionEvent -> handleButton3());
        button4.setOnAction(actionEvent -> handleButton4());
        saveButton.setOnAction(actionEvent -> handleSaveButton());

        nameField.setText(nameUser);
        emailField.setText(emailUser);
        darkModeCheckBox.setSelected(isDarkMode);
        notificationsCheckBox.setSelected(isNotification);

        if (isDarkMode) {
            root.getStylesheets().clear();
            root.getStylesheets().add(getClass().getResource("/CSSStyling/dark_settings.css").toExternalForm());

            pane.getStylesheets().clear();
            pane.getStylesheets().add(getClass().getResource("/CSSStyling/dark_settings.css").toExternalForm());

        } else {
            root.getStylesheets().clear();
            root.getStylesheets().add(getClass().getResource("/CSSStyling/settings.css").toExternalForm());

            pane.getStylesheets().clear();
            pane.getStylesheets().add(getClass().getResource("/CSSStyling/settings.css").toExternalForm());
        }
    }


    private void handleMainButton() {
        // Xử lý cho nút Home
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void handleSaveButton() {
        String name = nameField.getText();
        String email = emailField.getText();
        boolean darkMode = darkModeCheckBox.isSelected();
        boolean notifications = notificationsCheckBox.isSelected();

        if ((name != null && email != null)) {
            nameUser = name;
            emailUser = email;
            isDarkMode = darkMode;
            isNotification = notifications;
            initialize();
        } else {
            if (name == null) {
                System.out.println("Name is empty");
            }
            if (email == null) {
                System.out.println("Email is empty");
            }
        }
    }
}
