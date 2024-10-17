package com.example.library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.scene.Parent;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextArea messageArea;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private AnchorPane anchorPane;

    private Map<String, String> userDatabase = new HashMap<>();

    @FXML
    public void initialize() {
        // Tải hình ảnh và thiết lập cho ImageView
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/library.png")));
        backgroundImage.setImage(image);
        
        // Đặt kích thước cho ImageView
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());
        
        registerButton.setOnAction(e -> register());
        loginButton.setOnAction(e -> switchToLogin());
    }

    private void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageArea.setText("Tên đăng nhập và mật khẩu không được để trống.");
            return;
        }

        if (userDatabase.containsKey(username)) {
            messageArea.setText("Tên đăng nhập đã tồn tại.");
        } else {
            userDatabase.put(username, password); // Lưu thông tin người dùng
            messageArea.setText("Đăng ký thành công!");
        }
    }

    private void switchToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 924);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
