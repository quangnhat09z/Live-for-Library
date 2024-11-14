package org.example.library.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.library.Application.DatabaseHelper;

public class LoginController {

    @FXML
    private Text alertLoginSuccess;
    @FXML
    private Text alertLoginFail;
    @FXML
    private Text alertRegister;
    @FXML
    private Text alertExisting;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button backButton;
    @FXML
    private Button register_temp_Button;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticate(username, password)) {
            System.out.println("Login successful!");
            alertLoginSuccess.setVisible(true);
            alertRegister.setVisible(false);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("manage-documents.fxml"));
                Scene mainscene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(mainscene);
                stage.setTitle("Mien Nhat Phuoc");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Login failed.");
            register_temp_Button.setVisible(true);
            alertLoginFail.setVisible(true);
            alertRegister.setVisible(false);
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            alertLoginFail.setVisible(true);
            alertLoginFail.setText("Please enter both username and password.");
            return;
        }

        if (isUserExist(username)) {
            alertLoginFail.setVisible(false);
            alertLoginSuccess.setVisible(false);
            alertExisting.setVisible(true);
            alertRegister.setVisible(false);
        } else {
            registerUser(username, password);
            alertLoginFail.setVisible(false);
            alertLoginSuccess.setVisible(false);
            alertExisting.setVisible(false);
            alertRegister.setVisible(true);
            loginButton.setVisible(true);
            backButton.setVisible(false);
            registerButton.setVisible(false);
            System.out.println("User registered: " + username);
            usernameField.clear();
            passwordField.clear();
        }
    }

    private boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Trả về true nếu có kết quả
        } catch (Exception e) {
            System.out.println("Authentication error: " + e.getMessage());
            return false;
        }
    }

    private boolean isUserExist(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Trả về true nếu có kết quả
        } catch (Exception e) {
            System.out.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    private void registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        loginButton.setVisible(true);
        backButton.setVisible(false);
        register_temp_Button.setVisible(false);
        registerButton.setVisible(false);
        usernameField.clear();
        passwordField.clear();
        alertLoginFail.setVisible(false);
        alertExisting.setVisible(false);
        alertRegister.setVisible(false);
    }

    @FXML
    private void handleTempRegister() {
        usernameField.clear();
        passwordField.clear();
        registerButton.setVisible(true);
        register_temp_Button.setVisible(false);
        loginButton.setVisible(false);
        alertLoginFail.setVisible(false);
        backButton.setVisible(true);
    }
}