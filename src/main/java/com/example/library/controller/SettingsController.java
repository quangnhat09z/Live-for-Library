package com.example.library.controller;

import com.example.library.model.ChangeView;
import com.example.library.model.SoundUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.Objects;

import static com.example.library.model.SoundUtil.*;

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
    private Button saveButton;
    @FXML
    private Slider musicVolumeSlider;
    @FXML
    private Slider sfxVolumeSlider;
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
        saveButton.setOnAction(actionEvent -> handleSaveButton());

        nameField.setText(nameUser);
        emailField.setText(emailUser);
        darkModeCheckBox.setSelected(darkMode);
        notificationsCheckBox.setSelected(notification);

        // Liên kết musicVolumeSlider với âm lượng của MediaPlayer
        MediaPlayer mediaPlayer = MainController.getMediaPlayer();
        if (mediaPlayer != null) {
            // Đặt giá trị ban đầu cho thanh trượt
            musicVolumeSlider.setValue(mediaPlayer.getVolume() * 100);

            // Lắng nghe sự thay đổi giá trị của thanh trượt
            musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                double volume = newValue.doubleValue() / 100.0;
                if (volume >= 0.0 && volume <= 1.0) {
                    mediaPlayer.setVolume(volume); // Cập nhật âm lượng
                }
            });
        }

        // Liên kết sfxVolumeSlider với âm lượng của hiệu ứng âm thanh
        sfxVolumeSlider.setValue(SoundUtil.getHoverMediaPlayer().getVolume() * 100); // Đặt giá trị ban đầu
        sfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100.0;
            setSfxVolume(volume);
        });

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

        applySoundEffectsToButtons(root);
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    private void handleHomeButton() {
//        changeScene("/com/example/library/main-view.fxml", "Live for Library");
        Stage stage = (Stage) homeButton.getScene().getWindow();
        ChangeView.changeViewFXML("/com/example/library/main-view.fxml", stage);
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
