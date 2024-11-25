package com.example.library.controller;

import static com.example.library.model.Validator.checkEmail;
import static com.example.library.model.Validator.checkPassword;
import static com.example.library.model.Validator.checkUsername;

import com.example.library.model.DatabaseHelper;
import com.example.library.model.Validator;
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
import javafx.stage.Stage;


public class Login_RegisterController {

  @FXML
  private Button cancelButton;
  @FXML
  private TextField usernameTextile;
  @FXML
  private TextField passwordTextile;
  @FXML
  private TextField passwordTextField;

  @FXML
  TextField emailTextile;

  @FXML
  private CheckBox showPasswordCheckBox;
  @FXML
  private Label registerMessage;



  @FXML
  public void initialize() {
    // Request focus on emailTextile when the controller is initialized
    emailTextile.requestFocus();
    usernameTextile.setOnKeyPressed(this::handleEnterKey);
    passwordTextile.setOnKeyPressed(this::handleEnterKey);
    emailTextile.setOnKeyPressed(this::handleEnterKey);

    //Dong bo password
    passwordTextField.textProperty().bindBidirectional(passwordTextile.textProperty());
    showPasswordCheckBox.setOnAction(event -> togglePasswordVisibility());
  }

  private void togglePasswordVisibility() {
    if (showPasswordCheckBox.isSelected()) {
      passwordTextField.setVisible(true);
      passwordTextField.setManaged(true);
      passwordTextile.setVisible(false);
      passwordTextile.setManaged(false);
    } else {
      passwordTextField.setVisible(false);
      passwordTextField.setManaged(false);
      passwordTextile.setVisible(true);
      passwordTextile.setManaged(true);
    }
  }

  private void handleEnterKey(KeyEvent event) {
    // If Enter key is pressed, trigger the login button action
    if (event.getCode().equals(javafx.scene.input.KeyCode.ENTER)) {
      registerButtonOnAction(new ActionEvent());
    }
  }


  public void cancelButtonOnAction(ActionEvent e) {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  public void insertUser(String email, String role, String username, String password) {

    String query = "INSERT INTO accounts (email, role, username, password) VALUES (?, ?, ?, ?)";
    try (Connection connection = DatabaseHelper.connect();
        PreparedStatement pstm = connection.prepareStatement(query)) {
      pstm.setString(1, email);
      pstm.setString(2, role);
      pstm.setString(3, username);
      pstm.setString(4, password);

      pstm.executeUpdate();
      System.out.println("Account inserted successfully!");
    } catch (SQLException e) {
      System.out.println("Error while inserting account!");
      e.printStackTrace();
    }
  }

  public boolean isUsernameTaken(String username) {

    String query = "SELECT * FROM accounts WHERE username = ?";
    try(Connection connection=DatabaseHelper.connect();
   PreparedStatement pstmt= connection.prepareStatement(query)){
      pstmt.setString(1,username);
      ResultSet resultSet= pstmt.executeQuery();
      return resultSet.next();
    } catch (SQLException e){
      System.out.println("Error checking username!");
      e.printStackTrace();
      return false;
    }
  }
  public boolean isEmailTaken(String email) {

    String query = "SELECT * FROM accounts WHERE email = ?";
    try(Connection connection=DatabaseHelper.connect();
        PreparedStatement pstmt= connection.prepareStatement(query)){
      pstmt.setString(1,email);
      ResultSet resultSet= pstmt.executeQuery();
      return resultSet.next();
    } catch (SQLException e){
      System.out.println("Error checking email!");
      e.printStackTrace();
      return false;
    }
  }

  public void registerButtonOnAction(ActionEvent e) {
    String email = emailTextile.getText();
    String username = usernameTextile.getText();
    String password = passwordTextile.getText();
    Validator validator = new Validator();
    boolean check_email = true;
    boolean check_username = true;
    boolean check_password = true;
    if (!checkEmail(email)) {
      registerMessage.setText("Email must be @gmail.com");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
      emailTextile.requestFocus();
      return;
    }
    if (!checkUsername(username)) {
      registerMessage.setText("Invalid username");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
      usernameTextile.requestFocus();
      return;
    }
    if (!checkPassword(password)) {
      registerMessage.setText(
          "Password must have at least 1 uppercase letter, 1 special character and at least 8 characters");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
      passwordTextile.requestFocus();
      return;
    }

    if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
      registerMessage.setText("Please fill in all fields.");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
    } else if (isUsernameTaken(username)) {
      registerMessage.setText("Username is already taken. Please choose another.");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
    } else if (isEmailTaken(email)) {
      registerMessage.setText("Username is already taken. Please choose another.");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
    } else {
      insertUser(email, "user", username, password);
      registerMessage.setText("Register Successfully");
      registerMessage.setTextFill(javafx.scene.paint.Color.GREEN);
      giveBlankSpace();
    }
  }

  public void backLoginButtonOnAction(ActionEvent e) {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/com/example/library/login-login-view.fxml"));

      Parent root = loader.load();
      Scene loginScene = new Scene(root);

      Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
      stage.setScene(loginScene);
      stage.show();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void giveBlankSpace() {
    if (usernameTextile != null) {
      usernameTextile.clear();
    }
    if (passwordTextile != null) {
      passwordTextile.clear();
    }
    if (emailTextile != null) {
      emailTextile.clear();
    }
  }
}
