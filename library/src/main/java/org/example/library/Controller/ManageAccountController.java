package org.example.library.Controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import org.example.library.Application.Account;
import org.example.library.Application.DatabaseHelper;

public class ManageAccountController {

  @FXML
  private TextField usernameField;
  @FXML
  private TextField passwordField;

  @FXML
  private ComboBox<String> roleComboBox;
  @FXML
  private ListView<String> accountListView;
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
  }

  public void updateAccountList() {
    List<Account> accounts = dbHelper.getAllAcounts();
    ObservableList<String> accountStrings = FXCollections.observableArrayList();
    for (Account acc : accounts) {
      accountStrings.add(acc.getId() + " - " + acc.getUsername() + " - " + acc.getPassword() + " - "
          + acc.getRole());
    }
    accountListView.setItems(accountStrings);
  }


  @FXML
  public void onAddAccountClick() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    String role = roleComboBox.getSelectionModel().getSelectedItem();

    if (username.isEmpty() || password.isEmpty() || role == null) {
      showWarningAlert("Vui lòng điền đầy đủ thông tin.");
      return;
    }
    try {
      Account newAccount = new Account(0, username, password, role);
      dbHelper.addAccount(newAccount, this);
      showSuccessAlert("Add Account Successfully");
      updateAccountList();
      giveBlackSpace();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Have some problems when add Account");
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
          showSuccessAlert("Account have been deleted");
        } catch (SQLException e) {
          e.printStackTrace();
          showWarningAlert("Have some problems when delete Account");
        }
      } else {
        showWarningAlert("Cancel delete Account");
      }
    } else {
      showWarningAlert("Please choose Account to delete");
    }
  }

  public void onChangeToSearchingClick(ActionEvent event) {
  }


  public void giveBlackSpace() {
    usernameField.clear();
    passwordField.clear();
    roleComboBox.getItems().addAll("admin", "user");
  }

  public void clearUsernameField() {
    usernameField.clear();
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
}
