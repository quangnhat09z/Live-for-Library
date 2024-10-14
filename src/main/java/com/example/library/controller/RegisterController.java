package com.example.library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    private Map<String, String> userDatabase = new HashMap<>(); // Lưu trữ thông tin người dùng

    private Parent root; // Biến root
    private Scene scene; // Biến scene
    private Stage stage; // Biến stage

    @FXML
    public void initialize() {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/view/LoginView.fxml"));
            root = loader.load();
            stage = (Stage) loginButton.getScene().getWindow();
            scene = new Scene(root, 300, 250);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
