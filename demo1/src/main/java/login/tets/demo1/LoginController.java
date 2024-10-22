package login.tets.demo1;

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

public class LoginController {

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
        "SELECT COUNT(1) FROM UserAccount WHERE username='" + username_Textile.getText()
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
          loginMessage.setText("Invalid Login.Please try again");
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
      // Tải tệp FXML của giao diện đăng nhập
      FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));

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
