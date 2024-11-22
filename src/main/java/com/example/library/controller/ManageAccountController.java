package com.example.library.controller;

import static com.example.library.model.Validator.checkEmail;
import static com.example.library.model.Validator.checkPassword;
import static com.example.library.model.Validator.checkUsername;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import com.example.library.model.Account;
import com.example.library.model.DatabaseHelper;

public class ManageAccountController {

  @FXML
  private TextField usernameField;
  @FXML
  private TextField passwordField;
  @FXML
  private TextField emailField;
  @FXML
  private ComboBox<String> roleComboBox;
  @FXML
  private ListView<String> accountListView;
  @FXML
  private Label ManageAccountMessage;
  private DatabaseHelper dbHelper;

  public void initialize() {
    dbHelper = new DatabaseHelper();
    roleComboBox.setItems(FXCollections.observableArrayList("admin", "user"));
  }

  //  @FXML
//  public void initialize() {
//    roleComboBox.setItems(FXCollections.observableArrayList("admin", "user"));
//  }
//  public String getSelectedRole() {
//    return roleComboBox.getSelectionModel().getSelectedItem();
//  }
  @FXML
  public void onShowAccountClick(ActionEvent actionEvent) {
    updateAccountList();
    giveBlackSpace();
    ManageAccountMessage.setText("");
    ManageAccountMessage.setTextFill(javafx.scene.paint.Color.GREEN);
  }

  public void updateAccountList() {
    List<Account> accounts = dbHelper.getAllAcounts();
    ObservableList<String> accountStrings = FXCollections.observableArrayList();
    for (Account acc : accounts) {
      accountStrings.add(acc.getId() + " - " + acc.getUsername() + " - " + acc.getPassword() + " - "
          + acc.getEmail() + " - "
          + acc.getRole());
    }
    accountListView.setItems(accountStrings);
  }


  @FXML
  public void onAddAccountClick() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    String role = roleComboBox.getSelectionModel().getSelectedItem();
    String email = emailField.getText();

    if (username.isEmpty() || password.isEmpty() || role == null) {
      ManageAccountMessage.setText("Please fill all the blank!");
      ManageAccountMessage.setTextFill(javafx.scene.paint.Color.RED);
      return;
    }
    if (!checkEmail(email)) {
      ManageAccountMessage.setText("Email must be @gmail.com!");
      ManageAccountMessage.setTextFill(javafx.scene.paint.Color.RED);
      emailField.clear();
      emailField.requestFocus();
      return ;
    }
    if (!checkUsername(username)) {
      ManageAccountMessage.setText("Invalid username!");
      ManageAccountMessage.setTextFill(javafx.scene.paint.Color.RED);
      usernameField.clear();
      usernameField.requestFocus();
      return;
    }
    if (!checkPassword(password)) {
      ManageAccountMessage.setText(
          "Password must have at least 1 uppercase letter, 1 special character and at least 8 characters!");
      ManageAccountMessage.setTextFill(javafx.scene.paint.Color.RED);
      passwordField.clear();
      passwordField.requestFocus();
      return;
    }


    try {
      Account newAccount = new Account(0, username, password, email, role);
      dbHelper.addAccount(newAccount, this);
      ManageAccountMessage.setText("Add Account Successfully!");
      ManageAccountMessage.setTextFill(javafx.scene.paint.Color.GREEN);
      updateAccountList();
      giveBlackSpace();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Have some problems when add Account!");
    }
  }

  @FXML
  public void onDeleteAccountClick() {
    String selectedAccount = accountListView.getSelectionModel().getSelectedItem();
    if (selectedAccount != null) {
      int id = Integer.parseInt(
          selectedAccount.split(
              " - ")[0]); // Trả về phần tử đầu tiên của dòng (được thể hiện bằng bảng) listview mình vừa chọn=> lấy được ID

      //Hỏi lại lần nữa xem chắc chưa
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Delete Account");
      dialog.setHeaderText("Do you want to delete your account?");
      dialog.setContentText("Enter 'YES' to confirm");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent() && result.get().equalsIgnoreCase("YES")) {
        try {
          dbHelper.deleteAccount(id);
          updateAccountList();
          ManageAccountMessage.setText("Account have been deleted!");
          ManageAccountMessage.setTextFill(javafx.scene.paint.Color.GREEN);
        } catch (SQLException e) {
          e.printStackTrace();
          ManageAccountMessage.setText("Have some problems when delete Account!");
          ManageAccountMessage.setTextFill(javafx.scene.paint.Color.RED);
        }
      } else {
        ManageAccountMessage.setText("Cancel delete Account!");
        ManageAccountMessage.setTextFill(javafx.scene.paint.Color.RED);

      }
    } else {
      ManageAccountMessage.setText("Please choose account to delete!");
      ManageAccountMessage.setTextFill(javafx.scene.paint.Color.RED);
    }
    giveBlackSpace();
  }

  public void onChangeToSearchingClick(ActionEvent event) {
  }


  public void giveBlackSpace() {
    usernameField.clear();
    passwordField.clear();
    emailField.clear();
    roleComboBox.getItems().clear();
    roleComboBox.setPromptText("Role");
    roleComboBox.getItems().addAll("admin", "user");
  }


  public void clearUsernameField() {
    usernameField.clear();
  }

  public void clearEmailField() {
    emailField.clear();
  }

  public static void showWarningAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setHeaderText("WARNING");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showSuccessAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION); // Sử dụng INFORMATION
    alert.setTitle("Success");
    alert.setHeaderText("SUCCESS");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showInfoAlert(String title, String header, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait(); // Hiển thị và chờ người dùng đóng
  }

  public void onChangeToSearchView(ActionEvent event) {
  }
}
