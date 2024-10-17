package com.example.library.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    private ImageView backgroundImage;
    @FXML
    private AnchorPane anchorPane; // Thêm biến AnchorPane

    private Map<String, String> userDatabase = new HashMap<>();

    @FXML
    public void initialize() {
        // Tải hình ảnh và thiết lập cho ImageView
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/library.png"))); // Đường dẫn đến hình ảnh
        backgroundImage.setImage(image);
        
        // Đặt kích thước cho ImageView
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());
        
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/RegisterView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 924);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
