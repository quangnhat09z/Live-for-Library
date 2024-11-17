package org.example.library.Controller;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.library.Application.DatabaseConnection;

public class Login_RegisterController {

  @FXML
  private Button cancelButton;
  @FXML
  private TextField usernameTextile;
  @FXML
  private TextField passwordTextile;
  @FXML
  TextField emailTextile;
  @FXML
  private Label registerMessage;


  @FXML
  public void initialize() {
    // Request focus on emailTextile when the controller is initialized
    emailTextile.requestFocus();
    usernameTextile.setOnKeyPressed(this::handleEnterKey);
    passwordTextile.setOnKeyPressed(this::handleEnterKey);
    emailTextile.setOnKeyPressed(this::handleEnterKey);
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
    DatabaseConnection connectionClass = new DatabaseConnection();
    Connection connection = connectionClass.getConnection();

    String query = "INSERT INTO accounts (email, role, username, password) VALUES (?, ?, ?, ?)";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setString(1, email);
      pstmt.setString(2, role);
      pstmt.setString(3, username);
      pstmt.setString(4, password);

      pstmt.executeUpdate();
      System.out.println("User inserted successfully!");

    } catch (SQLException e) {
      System.out.println("Error while inserting user!");
      e.printStackTrace();
    }
  }

  public boolean isUsernameTaken(String username) {
    DatabaseConnection connectionClass = new DatabaseConnection();
    Connection connection = connectionClass.getConnection();

    String query = "SELECT * FROM accounts WHERE username = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setString(1, username);
      ResultSet resultSet = pstmt.executeQuery();

      // If we get a result, the username is already taken
      if (resultSet.next()) {
        return true;
      }
    } catch (SQLException e) {
      System.out.println("Error checking username!");
      e.printStackTrace();
    }

    return false;
  }

  public boolean isEmailTaken(String email) {
    DatabaseConnection connectionClass = new DatabaseConnection();
    Connection connection = connectionClass.getConnection();

    String query = "SELECT * FROM accounts WHERE email = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setString(1, email);
      ResultSet resultSet = pstmt.executeQuery();

      // If we get a result, the username is already taken
      if (resultSet.next()) {
        return true;
      }
    } catch (SQLException e) {
      System.out.println("Error checking email!");
      e.printStackTrace();
    }

    return false;
  }

  public void registerButtonOnAction(ActionEvent e) {
    String email = emailTextile.getText();
    String username = usernameTextile.getText();
    String password = passwordTextile.getText();

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
      // Tải tệp FXML của giao diện đăng nhập
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/org/example/library/login-login-view.fxml"));

      // Tạo một Scene mới từ tệp FXML
      Parent root = loader.load();
      Scene loginScene = new Scene(root);

      // Lấy Stage hiện tại từ nút đã kích hoạt sự kiện
      Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

      // Đặt Scene mới cho Stage
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
