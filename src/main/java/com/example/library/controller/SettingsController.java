package com.example.library.controller;

import com.example.library.model.ChangeView;
import com.example.library.model.DatabaseHelper;
import com.example.library.model.SoundUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

import static com.example.library.model.SoundUtil.*;

public class SettingsController extends Controller {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox root;
    @FXML
    private Button homeButton;
    @FXML
    private Button bookButton;
    @FXML
    private Button exploreButton;
    @FXML
    private Button gameButton;
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
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField createdDateField;
    @FXML
    private TextField lastUpdateField;
    @FXML
    private CheckBox darkModeCheckBox;
    @FXML
    private CheckBox notificationsCheckBox;

    private static int id;
    private static boolean darkMode;

    @Override
    public void initialize() {
        homeButton.setOnAction(actionEvent -> handleHomeButton());
        bookButton.setOnAction(actionEvent -> handleBookButton());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        gameButton.setOnAction(actionEvent -> handleGameButton());
        saveButton.setOnAction(actionEvent -> handleSaveButton());

        getInfo();

//        nameField.setText(name);
//        emailField.setText(email);
//        darkModeCheckBox.setSelected(darkMode);
//        notificationsCheckBox.setSelected(notification);

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
//            System.out.println("ID is now: " + id);

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

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        SettingsController.id = id;
    }

    private void getInfo() {
        String query = "SELECT full_name, email, username, password, created_at, updated_at, dark_mode, notification " +
                "FROM accounts left join user_verification on accounts.id = user_verification.id WHERE accounts.id = ?;";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, SettingsController.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("full_name");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String created = rs.getString("created_at");
                String updated = rs.getString("updated_at");
                boolean darkMode = rs.getBoolean("dark_mode");
                boolean notifications = rs.getBoolean("notification");

                nameField.setText(name);
                emailField.setText(email);
                usernameField.setText(username);
                passwordField.setText(password);
                createdDateField.setText(created);
                lastUpdateField.setText(updated);
                darkModeCheckBox.setSelected(darkMode);
                notificationsCheckBox.setSelected(notifications);
                ;
                SettingsController.darkMode = darkMode;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleHomeButton() {
        changeScene("/com/example/library/main-view.fxml", "Live for Library");
    }

    @Override
    public void handleBookButton() {
        // Xử lý cho nút Button1
        System.out.println("Button1 clicked");
    }

    @Override
    public void handleExploreButton() {
        // Xử lý cho nút Button2
        changeScene("/com/example/library/explore-view.fxml", "Explore");
    }

    @Override
    public void handleGameButton() {
        // Xử lý cho nút Button3
        System.out.println("Button3 clicked");
    }

    private void handleSaveButton() {
        String name = nameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String created = createdDateField.getText();
        String updated = lastUpdateField.getText();
        boolean darkMode = darkModeCheckBox.isSelected();
        boolean notifications = notificationsCheckBox.isSelected();

        String query = "UPDATE accounts SET email = ?" +
                ", username = ?, password = ?, created_at = ?, updated_at = ?" +
                " WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(
                DatabaseHelper.URL, DatabaseHelper.USER, DatabaseHelper.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, created);
            pstmt.setString(5, updated);
//            pstmt.setBoolean(6, darkMode);
//            pstmt.setBoolean(7, notifications);
            pstmt.setInt(6, SettingsController.id);
            pstmt.executeUpdate();
            conn.close();
            System.out.println("Thông tin đã được lưu lại thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Có lỗi xảy ra khi lưu lại thông tin!");
        }

        String query2 = "Update user_verification set full_name = ?, dark_mode = ?, " +
                "notification = ? where id = ?";
        try (Connection conn = DriverManager.getConnection(
                DatabaseHelper.URL, DatabaseHelper.USER, DatabaseHelper.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query2)) {
            pstmt.setString(1, name);
            pstmt.setBoolean(2, darkMode);
            pstmt.setBoolean(3, notifications);
            pstmt.setInt(4, SettingsController.id);
            pstmt.executeUpdate();
            conn.close();
            System.out.println("Thông tin đã được lưu lại thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Có lỗi xảy ra khi lưu lại thông tin!");
        }
        initialize();



    }

    @Override
    protected void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) bookButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
