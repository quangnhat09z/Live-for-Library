package login.tets.demo1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterController {

  @FXML
  private Button cancelButton;
  @FXML
  private Button registerButton;
  @FXML
  private  Button BackLoginButton;;
  @FXML
  private TextField usernameTextile;
  @FXML
  private TextField passwordTextile;
  @FXML
  private TextField lastNameTextile;
  @FXML
  private TextField firstNameTextile;
  @FXML
  private Label registerMessage;

  public void cancelButtonOnAction(ActionEvent e) {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  public void insertUser(String firstname, String lastname, String username, String password) {
    DatabaseConnection connectionClass = new DatabaseConnection();
    Connection connection = connectionClass.getConnection();

    String query = "INSERT INTO userAccount (Firstname, lastname, username, password) VALUES (?, ?, ?, ?)";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
      pstmt.setString(1, firstname);
      pstmt.setString(2, lastname);
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

    String query = "SELECT * FROM userAccount WHERE username = ?";

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

  public void registerButtonOnAction(ActionEvent e) {
    String firstname = firstNameTextile.getText();
    String lastname = lastNameTextile.getText();
    String username = usernameTextile.getText();
    String password = passwordTextile.getText();

    if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty()) {
      registerMessage.setText("Please fill in all fields.");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
    } else if (isUsernameTaken(username)) {
      registerMessage.setText("Username is already taken. Please choose another.");
      registerMessage.setTextFill(javafx.scene.paint.Color.RED);
    } else {
      insertUser(firstname, lastname, username, password);
      registerMessage.setText("Register Successfully");
      registerMessage.setTextFill(javafx.scene.paint.Color.GREEN);
      firstNameTextile.clear();
      lastNameTextile.clear();
      usernameTextile.clear();
      passwordTextile.clear();
    }
  }

  public void backLoginButtonOnAction(ActionEvent e) {
    try {
      // Tải tệp FXML của giao diện đăng nhập
      FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));

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


}
