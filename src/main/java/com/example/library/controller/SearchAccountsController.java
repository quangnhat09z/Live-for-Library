package com.example.library.controller;

import static com.example.library.controller.UpdateController.showWarningAlert;
import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;

import com.example.library.model.AccountChangeInfor;
import com.example.library.model.AccountDetail;
import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;
import com.example.library.model.DocumentChangeInfo;
import com.example.library.model.searchAccount;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SearchAccountsController extends Controller {

  private DatabaseHelper databaseHelper;

  public SearchAccountsController() {
    databaseHelper = new DatabaseHelper();
  }

  @FXML
  private HBox root;

  // Field
  @FXML
  private TextField idField;
  @FXML
  private TextField emailField;
  @FXML
  private TextField usernameField;
  @FXML
  private TextField roleField;
  @FXML
  private TextField phoneField;


  //Column
  @FXML
  private TableView<AccountDetail> resultsTableView;
  @FXML
  private TableColumn<Document, String> idColumn;
  @FXML
  private TableColumn<Document, String> usernameColumn;
  @FXML
  private TableColumn<Document, String> passwordColumn;
  @FXML
  private TableColumn<Document, String> fullNameColumn;
  @FXML
  private TableColumn<Document, String> phoneNumberColumn;
  @FXML
  private TableColumn<Document, String> addressColumn;
  @FXML
  private TableColumn<Document, String> roleColumn;
  @FXML
  private TableColumn<Document, String> emailColumn;
  @FXML
  private TableColumn<Document, String> statusColumn;


  @FXML
  private Button homeButton;
  @FXML
  private Button bookButton;
  @FXML
  private Button exploreButton;
  @FXML
  private Button gameButton;
  @FXML
  private Button searchButton;
  @FXML
  private Button deleteButton;
  @FXML
  private Button changeButton;
  @FXML
  private Button changeInfo;

  @Override
  public void initialize() {
    String[] a = {"idColumn", "usernameColumn", "passwordColumn",
        "emailColumn", "fullNameColumn", "addressColumn", "roleColumn", "phoneColumn",
        "statusColumn"};
    int cnt = 0;
    if (idColumn != null) {
      idColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (usernameColumn != null) {
      usernameColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (passwordColumn != null) {
      passwordColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (emailColumn != null) {
      emailColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (fullNameColumn != null) {
      fullNameColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (addressColumn != null) {
      addressColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (roleColumn != null) {
      roleColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (phoneNumberColumn != null) {
      phoneNumberColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }
    cnt++;

    if (statusColumn != null) {
      statusColumn.setCellValueFactory(
          new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
    }

    //if (myButton != null ) initializeMoving();
    loadAccounts();
    homeButton.setOnAction(actionEvent -> handleHomeButton());
//    bookButton.setOnAction(actionEvent -> handleBookButton());
//    exploreButton.setOnAction(actionEvent -> handleExploreButton());
//    gameButton.setOnAction(actionEvent -> handleGameButton());
    searchButton.setOnAction(actionEvent -> handleSearchButton());
    deleteButton.setOnAction(actionEvent -> handleDeleteButton());
    changeButton.setOnAction(actionEvent -> handleChangeButton());
    applySoundEffectsToButtons(root);
  }

  private void handleChangeButton() {
    changeScene("/com/example/library/manage-accounts-view.fxml", " Manage Account");
  }

  private void handleSearchButton() {
    String id = idField.getText();
    String email = emailField.getText();
    String username = usernameField.getText();
    String role = roleField.getText();
    String phone = phoneField.getText();
    searchAccount search = new searchAccount();
//    searchAccounts(String id, String email, String username, String role,
//        String phone) {
    List<AccountDetail> results = search.searchAccounts(id, email, username, role, phone);
    // Check results
    if (results.isEmpty()) {
      showInfoAlert("NOTIFICATION", "NOT FIND!", "Not find any accounts like your details");
      System.out.println("No results found.");
    } else {
      System.out.println("Results found: " + results.size());
    }
    // Hiển thị kết quả tìm kiếm trong TableView
    ObservableList<AccountDetail> observableResults = FXCollections.observableArrayList(results);
    resultsTableView.setItems(observableResults);
  }


  @FXML
  public void handleDeleteButton() {
    String selectedAccount = String.valueOf(resultsTableView.getSelectionModel().getSelectedItem());

    // Kiểm tra nếu không có tài liệu nào được chọn
    if (selectedAccount == null || selectedAccount.equals("null")) {
      showWarningAlert("SELECT ACCOUNT FOR DELETING!");
    }

    try {
      int id = Integer.parseInt(selectedAccount.split(" - ")[0]); // Giả định format là "ID - Title"
      // Thực hiện xóa tài khoản
      databaseHelper.deleteAccount(id);
      loadAccounts();

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Having problem while Delete Account");
    }
  }


  public void onDeleteDocumentInSearchingClick(ActionEvent event) {
  }

  public void onChangeToManageClick(ActionEvent event) {
  }

  public void onSearchClick(ActionEvent event) {

  }


  @Override
  public void handleHomeButton() {
    changeScene("/com/example/library/main-view.fxml", "Live for Library");
  }

  @Override
  public void handleBookButton() {
    System.out.println("Button1 clicked");
  }

  @Override
  public void handleExploreButton() {
    changeScene("/com/example/library/explore-view.fxml", "Explore");
  }

  @Override
  public void handleGameButton() {
    System.out.println("Button3 clicked");
  }

  @Override
  protected void changeScene(String fxmlPath, String title) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      Parent root = loader.load();
      Stage stage = (Stage) deleteButton.getScene().getWindow();
      Scene scene = new Scene(root, 1300, 650);
      stage.setTitle(title);
      stage.setScene(scene);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void showInfoAlert(String title, String header, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait(); // Hiển thị và chờ người dùng đóng
  }

  @FXML
  public void onChangeInfoInSearchingClick() {
    AccountDetail selectedAccount = resultsTableView.getSelectionModel().getSelectedItem();
    if (selectedAccount != null) {
      System.out.println("da vo day");
      // Tạo hộp thoại
      Dialog<AccountDetail> dialog = new Dialog<>();
      dialog.setTitle("UPDATE ACCOUNT");
      dialog.setHeaderText("EDIT ACCOUNT INFORMATION");

      // Tạo các trường nhập liệu
      GridPane grid = new GridPane();
      grid.setHgap(15);
      grid.setVgap(15);
      grid.setPadding(new Insets(30, 150, 30, 30));

      TextField userName = new TextField(selectedAccount.getUsername());
      userName.setPrefWidth(400); // Chiều rộng ưa thích cho trường Tiêu đề

      TextField passWord = new TextField(selectedAccount.getPassword());
      passWord.setPrefWidth(400);

      TextField email = new TextField(String.valueOf(selectedAccount.getEmail()));
      email.setPrefWidth(400);

      TextField fullName = new TextField(selectedAccount.getFullName());
      fullName.setPrefWidth(400);

      TextField address = new TextField(selectedAccount.getAddress());
      address.setPrefWidth(400);

      TextField role = new TextField(String.valueOf(selectedAccount.getRole()));
      role.setPrefWidth(400);

      TextField phoneNumber = new TextField(selectedAccount.getPhoneNumber());
      phoneNumber.setPrefWidth(400);

      TextField status = new TextField(selectedAccount.getStatus());
      status.setPrefWidth(400);

      grid.add(new Label("USERNAME:"), 0, 0);
      grid.add(userName, 1, 0);
      grid.add(new Label("PASSWORD:"), 0, 1);
      grid.add(passWord, 1, 1);
      grid.add(new Label("EMAIL:"), 0, 2);
      grid.add(email, 1, 2);
      grid.add(new Label("FULLNAME:"), 0, 3);
      grid.add(fullName, 1, 3);
      grid.add(new Label("ADDRESS:"), 0, 4);
      grid.add(address, 1, 4);
      grid.add(new Label("ROLE:"), 0, 5);
      grid.add(role, 1, 5);
      grid.add(new Label("PHONE NUMBER:"), 0, 6);
      grid.add(phoneNumber, 1, 6);
      grid.add(new Label("STATUS"), 0, 7);
      grid.add(status, 1, 7);

      dialog.getDialogPane().setContent(grid);

      // Thêm nút "OK" và "Cancel"
      ButtonType okButton = new ButtonType("UPDATE", ButtonBar.ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

      // Xử lý kết quả khi nhấn nút "OK"
      dialog.setResultConverter(dialogButton -> {
        if (dialogButton == okButton) {

// public AccountDetail(int id, String username, String password, String email, String role,
//              String fullName, String address, String phoneNumber, String status)
          int id = selectedAccount.getId();
          String newUsername = userName.getText();
          String newPassword = passWord.getText();
          String newEmail = email.getText();
          String newRole = role.getText();
          String newFullName = fullName.getText();
          System.out.println("DA lay dc full name: " +fullName.getText());
          String newAddress = address.getText();
          String newPhoneNumber = phoneNumber.getText();
          String newStatus = status.getText();

          return new AccountDetail(
              id,
              newUsername,
              newPassword,
              newEmail,
              newRole,
              newFullName,
              newAddress,
              newPhoneNumber,
              newStatus
          );
        }
        return null;
      });

      // Hiển thị hộp thoại và lấy kết quả
      dialog.showAndWait().ifPresent(updatedAccount -> {
        AccountChangeInfor accountUTD = new AccountChangeInfor();
        boolean isUpdated = accountUTD.updateAccount(
            selectedAccount.getId(),
            updatedAccount.getUsername(),
            updatedAccount.getPassword(),
            updatedAccount.getEmail(),
            updatedAccount.getFullName(),
            updatedAccount.getAddress(),
            updatedAccount.getRole(),
            updatedAccount.getPhoneNumber(),
            updatedAccount.getStatus());

        if (isUpdated) {
          System.out.println("UPDATE SUCCESSFULLY!");
          loadAccounts();
          // Cập nhật lại danh sách tài liệu trong bảng
        } else {
          System.out.println("UPDATE FAILD!");
        }
      });
    } else {
      // Thông báo nếu không có tài liệu nào được chọn
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("NOTIFICATION");
      alert.setHeaderText("CAN NOT EDIT!");
      alert.setContentText("Select one account for editing!");
      alert.showAndWait();
    }
  }

  public void loadAccounts() {
    try {
      // Ensure searchAccount.searchAccounts returns a List<AccountDetail>
      List<AccountDetail> accounts = searchAccount.searchAccounts("", "", "", "", "");
      System.out.println("Accounts loaded: " + accounts);
      ObservableList<AccountDetail> accountList = FXCollections.observableArrayList(accounts);
      resultsTableView.setItems(accountList);
    } catch (Exception e) {
      e.printStackTrace();
      showWarningAlert("Error while loading accounts.");
    }
  }



}

