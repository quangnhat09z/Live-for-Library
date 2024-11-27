package com.example.library.controller.login;

import com.example.library.controller.mainScreen.Controller;
import com.example.library.controller.mainScreen.SettingsController;
import com.example.library.model.ChangeView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static com.example.library.model.DatabaseHelper.connect;

public class Login_LoginController {

    @FXML
    private Button cancelButton;
    @FXML
    private Button comeToRegister;
    @FXML
    public TextField username_Textile;
    @FXML
    private TextField password_Textile;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label loginMessage;
    @FXML
    private CheckBox showPasswordCheckBox;

    public static String usernameToBorrow = "f";

    @FXML
    public void initialize() {
        // Request focus on username_Textile when the controller is initialized
        username_Textile.requestFocus();
        username_Textile.setOnKeyPressed(this::handleEnterKey);
        password_Textile.setOnKeyPressed(this::handleEnterKey);

        passwordTextField.textProperty().bindBidirectional(password_Textile.textProperty());
        showPasswordCheckBox.setOnAction(event -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            password_Textile.setVisible(false);
            password_Textile.setManaged(false);
        } else {
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            password_Textile.setVisible(true);
            password_Textile.setManaged(true);
        }
    }



    private void handleEnterKey(KeyEvent event) {
        // If Enter key is pressed, trigger the login button action
        if (event.getCode().equals(javafx.scene.input.KeyCode.ENTER)) {
            loginButtonOnAction();
        }
    }

    public void loginButtonOnAction() {

        if (!username_Textile.getText().isBlank() && !password_Textile.getText().isBlank()) {
            validateLogin();
        } else {
            loginMessage.setText("Fill all the space!");
        }
    }

    public void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        String query = "SELECT COUNT(1) FROM accounts WHERE username = ? AND password = ?;";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username_Textile.getText());
            pstmt.setString(2, password_Textile.getText());
            ResultSet queryResult = pstmt.executeQuery();
            if (queryResult.next() && queryResult.getInt(1) == 1) {
                loginMessage.setText("Waiting for server...!");
                loginMessage.setTextFill(Color.GREEN);
                //Get id for setting
                String username = username_Textile.getText();
                usernameToBorrow = username;
                SettingsController.setId(getId(username));

                //Get role
                Controller.setAdmin(isAdmin(username));
                //Get dark_mode
                Controller.setDarkMode(isDark(username));

                username_Textile.clear();
                password_Textile.clear();

                // Chuyển sang giao diện chính
                Stage stage = (Stage) username_Textile.getScene().getWindow();
                ChangeView.changeViewFXML("/com/example/library/main-view.fxml", stage);
            } else {
                loginMessage.setText("Invalid Login. Please try again");
                loginMessage.setTextFill(Color.RED);
                username_Textile.clear();
                password_Textile.clear();
                comeToRegister.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            loginMessage.setText("Error connecting to the database");
            loginMessage.setTextFill(Color.RED);
        }

    }

    private boolean isAdmin(String username) {
        boolean result = false;
        int id = getId(username);
        String query = "select role from accounts where id = ?";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(query)) {

            checkStmt.setString(1, String.valueOf(id));
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                String s = resultSet.getString("role");
                if (s.equals("admin")) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isDark(String username) {
        boolean result = false;
        int id = getId(username);
        String query = "select dark_mode from user_verification where id = ?";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(query)) {

            checkStmt.setString(1, String.valueOf(id));
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getBoolean("dark_mode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int getId(String username) {
        int id = 0;
        String query = "SELECT accounts.id FROM accounts LEFT JOIN user_verification ON accounts.id = user_verification.id WHERE accounts.username = ?;";
        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(query)) {

            checkStmt.setString(1, username);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void comeRegisterOnAction(ActionEvent e) {
        try {
            // Load the FXML file for the login registration view
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/library/login-register-view.fxml"));


            // Create a new Scene from the FXML file
            Parent root = loader.load();
            Scene loginScene = new Scene(root);

            // Get the current Stage from the button that triggered the event
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // Set the new Scene on the Stage
            stage.setScene(loginScene);
            stage.show();
            Login_RegisterController registerController = loader.getController();
            registerController.emailTextile.requestFocus();  // Set focus on email field
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Co loi o day");
        }
    }
}
