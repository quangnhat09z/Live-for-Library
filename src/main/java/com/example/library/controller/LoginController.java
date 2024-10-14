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

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextArea messageArea;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private Map<String, String> userDatabase = new HashMap<>();

    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> login());
        registerButton.setOnAction(e -> switchToRegister());
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageArea.setText("Tên đăng nhập và mật khẩu không được để trống.");
            return;
        }

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            messageArea.setText("Đăng nhập thành công!");
        } else {
            messageArea.setText("Thông tin đăng nhập không chính xác.");
        }
    }

    private void switchToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main/resources/com/example/library/RegisterView.fxml"));
            root = loader.load();
            stage = (Stage) registerButton.getScene().getWindow();
            scene = new Scene(root, 300, 250);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            messageArea.setText("Không thể chuyển sang màn hình đăng ký: " + e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            messageArea.setText("Có lỗi xảy ra: " + e.getMessage());
        }
    }
}
