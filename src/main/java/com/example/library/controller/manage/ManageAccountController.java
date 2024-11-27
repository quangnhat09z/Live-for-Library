package com.example.library.controller.manage;

import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;
import static com.example.library.model.Validator.checkEmail;
import static com.example.library.model.Validator.checkPassword;
import static com.example.library.model.Validator.checkUsername;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.library.controller.mainScreen.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.library.model.Account;
import com.example.library.model.DatabaseHelper;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ManageAccountController extends ManageController {
  @FXML
  private HBox root;
  @FXML
  private Button homeButton;
  @FXML
  private Button bookButton;
  @FXML
  private Button exploreButton;
  @FXML
  private Button gameButton;
  @FXML
  private Button addButton;
  @FXML
  private Button showButton;
  @FXML
  private Button deleteButton;
  @FXML
  private Button searchButton;
  @FXML
  private Button documentsButton;
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
  private DatabaseHelper databaseHelper;

  @Override
  public void initialize() {
    databaseHelper = new DatabaseHelper();
    roleComboBox.setItems(FXCollections.observableArrayList("admin", "user"));

    homeButton.setOnAction(actionEvent -> handleHomeButton());
    bookButton.setOnAction(actionEvent -> handleBookButton());
    exploreButton.setOnAction(actionEvent -> handleExploreButton());
    gameButton.setOnAction(actionEvent -> handleGameButton());
    showButton.setOnAction(actionEvent -> handleShowButton());
    addButton.setOnAction(actionEvent -> handleAddButton());
    deleteButton.setOnAction(actionEvent -> handleDeleteButton());
    searchButton.setOnAction(actionEvent -> handleSearchButton());
    documentsButton.setOnAction(actionEvent -> handleChangeButton());

    if (Controller.isDarkMode()) {
      root.getStylesheets().clear();
      root.getStylesheets().add(Objects.requireNonNull(
              getClass().getResource("/CSSStyling/dark_manage.css")).toExternalForm());

    } else {
      root.getStylesheets().clear();
      root.getStylesheets().add(Objects.requireNonNull(
              getClass().getResource("/CSSStyling/manage.css")).toExternalForm());
    }

    applySoundEffectsToButtons(root);
  }

  //  @FXML
//  public void initialize() {
//    roleComboBox.setItems(FXCollections.observableArrayList("admin", "user"));
//  }
//  public String getSelectedRole() {
//    return roleComboBox.getSelectionModel().getSelectedItem();
//  }

  @Override
  protected void handleShowButton() {
    updateAccountList();
    giveBlackSpace();
    ManageAccountMessage.setText("");
    ManageAccountMessage.setTextFill(javafx.scene.paint.Color.GREEN);
  }

  @Override
  protected void handleAddButton() {
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
      databaseHelper.addAccount(newAccount, this);
      ManageAccountMessage.setText("Add Account Successfully!");
      ManageAccountMessage.setTextFill(javafx.scene.paint.Color.GREEN);
      updateAccountList();
      giveBlackSpace();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Have some problems when add Account!");
    }
  }

  @Override
  protected void handleDeleteButton() {
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
          databaseHelper.deleteAccount(id);
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

  @Override
  protected void handleSearchButton() {
    changeScene("/com/example/library/search-accounts-view.fxml", "Search Accounts");
  }

  @Override
  protected void handleChangeButton() {
    changeScene("/com/example/library/manage-documents-view.fxml", "Manage");
  }

  public void updateAccountList() {
    List<Account> accounts = databaseHelper.getAllAcounts();
    ObservableList<String> accountStrings = FXCollections.observableArrayList();
    for (Account acc : accounts) {
      accountStrings.add(acc.getId() + " - " + acc.getUsername() + " - " + acc.getPassword() + " - "
              + acc.getEmail() + " - "
              + acc.getRole());
    }
    accountListView.setItems(accountStrings);
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

  //
//  public void onChangeToSearchView(ActionEvent event) {
//    Stage stage = (Stage)usernameField.getScene().getWindow();
//    ChangeView.changeViewFXML("/com/example/library/.search-accounts.fxml", stage);
//  }

  @Override
  public void changeScene(String fxmlPath, String title) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      Parent root = loader.load();
      Stage stage = (Stage) usernameField.getScene().getWindow();
      Scene scene = new Scene(root, 1300, 650);
      stage.setTitle(title);
      stage.setScene(scene);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
