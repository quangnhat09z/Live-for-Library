package com.example.library.controller;
import com.example.library.model.ChangeView;
import com.example.library.model.DatabaseHelper;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class Login_LoginController {

  @FXML
  private Button cancelButton;
  @FXML
  private  Button comeToRegister;
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
    String query="SELECT COUNT(1) FROM accounts WHERE username = ? AND password = ?;";
    try(Connection conn = DatabaseHelper.connect();
        PreparedStatement pstmt= conn.prepareStatement(query)){
      pstmt.setString(1,username_Textile.getText());
      pstmt.setString(2,password_Textile.getText());
      ResultSet queryResult= pstmt.executeQuery();
      if(queryResult.next() && queryResult.getInt(1)==1 ){
        loginMessage.setText("Waiting for server...!");
        loginMessage.setTextFill(Color.GREEN);
        username_Textile.clear();
        password_Textile.clear();

        // Chuyển sang giao diện chính
        Stage stage = (Stage) username_Textile.getScene().getWindow();
        ChangeView.changeViewFXML("/com/example/library/main-view.fxml", stage);
      }else{
        loginMessage.setText("Invalid Login. Please try again");
        loginMessage.setTextFill(Color.RED);
        username_Textile.clear();
        password_Textile.clear();
        comeToRegister.setVisible(true);
      }
    } catch (SQLException e){
      e.printStackTrace();
      loginMessage.setText("Error connecting to the database");
      loginMessage.setTextFill(Color.RED);
    }

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
