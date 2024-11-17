package org.example.library.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
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
import org.example.library.Application.DatabaseConnection;
import javafx.scene.input.KeyEvent;

public class Login_LoginController {

  @FXML
  private Button cancelButton;
  @FXML
  private Button loginButton;
  @FXML
  private  Button cometoRegister;
  @FXML
  private TextField username_Textile;
  @FXML
  private TextField password_Textile;

  @FXML
  private Label loginMessage;

  @FXML
  public void initialize() {
    // Request focus on username_Textile when the controller is initialized
    username_Textile.requestFocus();
    username_Textile.setOnKeyPressed(this::handleEnterKey);
    password_Textile.setOnKeyPressed(this::handleEnterKey);
  }

  private void handleEnterKey(KeyEvent event) {
    // If Enter key is pressed, trigger the login button action
    if (event.getCode().equals(javafx.scene.input.KeyCode.ENTER)) {
      loginButtonOnAction(new ActionEvent());
    }
  }

  public void loginButtonOnAction(ActionEvent e) {

    if (!username_Textile.getText().isBlank() && !password_Textile.getText().isBlank()) {
      validateLogin();
    } else {
      loginMessage.setText("Fill all the space!");
    }
  }

  public void cancelButtonOnAction(ActionEvent e) {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  public void validateLogin() {
    DatabaseConnection connectNow = new DatabaseConnection();
    Connection connectDB = connectNow.getConnection();
    String verifyLogin =
        "SELECT COUNT(1) FROM accounts WHERE username='" + username_Textile.getText()
            + "' AND password='" + password_Textile.getText() + "';";
    try{
      Statement statement=connectDB.createStatement();
      ResultSet queryResult= statement.executeQuery(verifyLogin);
      while(queryResult.next()){
        if(queryResult.getInt(1)==1){
          loginMessage.setText("Login Successfully!");
          loginMessage.setTextFill(Color.GREEN);
          username_Textile.clear();
          password_Textile.clear();
        }else{
          loginMessage.setText("Invalid Login. Please try again");
          loginMessage.setTextFill(Color.RED);
          username_Textile.clear();
          password_Textile.clear();
          cometoRegister.setVisible(true);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void cometoRegisterOnAction(ActionEvent e) {
    try {
      // Load the FXML file for the login registration view
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/org/example/library/login-register-view.fxml"));


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
