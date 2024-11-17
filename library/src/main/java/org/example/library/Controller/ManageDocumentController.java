package org.example.library.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.library.Application.DatabaseHelper;
import org.example.library.Application.Document;
import org.json.JSONArray;
import org.json.JSONObject;

public class ManageDocumentController {

  @FXML
  private Label welcomeText;

  @FXML
  private TextField searchField;

  @FXML
  private TextArea resultText;

  @FXML
  private Button searchButton;


  @FXML
  private ListView<String> resultsListView;

  @FXML
  private ListView<String> documentListView;


  private DatabaseHelper databaseHelper = new DatabaseHelper(); // Initialize DatabaseHelper

  private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key

  @FXML
  protected void onHelloButtonClick() {
    welcomeText.setText("Nhập tên cuốn sách bạn mong muốn rồi ấn Tìm kiếm nhé!");
    searchField.setVisible(true);
    searchField.requestFocus();
    searchButton.setVisible(true);
  }

  @FXML
  public void onSearchButtonClick(ActionEvent actionEvent) {
    String searchTerm = searchField.getText();
    if (!searchTerm.isEmpty()) {
      String response = searchBooks(searchTerm);
      displayResults(response);
    } else {
      resultText.setText("Vui lòng nhập từ khóa tìm kiếm.");
    }
  }

  private String searchBooks(String searchTerm) {
    StringBuilder response = new StringBuilder();
    String urlString = "https://www.googleapis.com/books/v1/volumes?q=" +
        searchTerm.replace(" ", "+") + "&key=" + API_KEY;

    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      int responseCode = conn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
      } else {
        response.append("Error: ").append(responseCode);
      }
    } catch (IOException e) {
      response.append("Exception: ").append(e.getMessage());
    }

    return response.toString();
  }

  private void displayResults(String response) {
    try {
      JSONObject jsonResponse = new JSONObject(response);
      JSONArray items = jsonResponse.optJSONArray("items");

      if (items != null && items.length() > 0) {
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < items.length(); i++) {
          JSONObject book = items.getJSONObject(i);
          JSONObject volumeInfo = book.getJSONObject("volumeInfo");

          String title = volumeInfo.getString("title");
          String description = volumeInfo.optString("description", "Không có mô tả.");

          results.append("Tiêu đề: ").append(title).append("\n")
              .append("Mô tả: ").append(description).append("\n\n");
        }
        resultText.setText(results.toString());
      } else {
        resultText.setText("Không tìm thấy sách nào.");
      }
    } catch (Exception e) {
      resultText.setText("Lỗi khi phân tích dữ liệu.");
      e.printStackTrace();
    }
  }

  @FXML
  private TextField idField;
  @FXML
  private TextField titleField;
  @FXML
  private TextField authorField;
  @FXML
  private TextField publicYearField;
  @FXML
  private TextField publisherField;
  @FXML
  private TextField genreField;
  @FXML
  private TextField quantityField;

  @FXML
  private void onAddDocumentClick() {
    String title = titleField.getText().trim();
    String author = authorField.getText().trim();
    String publicYear = publicYearField.getText().trim();
    String publisher = publisherField.getText().trim();
    String genre = genreField.getText().trim();
    String quantityText = quantityField.getText().trim();

    if (title.isEmpty() || author.isEmpty() || publicYear.isEmpty() || publisher.isEmpty()
        || genre.isEmpty() || quantityText.isEmpty()) {
      showWarningAlert("Vui lòng nhập đầy đủ thông tin.");
      return;
    }

    try {
      int quantity = Integer.parseInt(quantityText);
      int id = 0; // Hoặc để id = -1 nếu bạn không có giá trị cụ thể
      Document document = new Document(id, title, author, publicYear, publisher, genre, quantity);
      databaseHelper.addDocument(document); // Gọi phương thức addDocument
      updateDocumentList();
      showWarningAlert("Add documents successfully");
      giveBlackSpace();
    } catch (NumberFormatException e) {
      showWarningAlert("Quantity must be a valid number");
    } catch (SQLException e) {
      showWarningAlert("Lỗi khi thêm tài liệu: " + e.getMessage());
    }
  }

  public void onShowDocumentsClick(ActionEvent actionEvent) {
    updateDocumentList(); //Update
  }

  private void updateDocumentList() {
    List<Document> documents = databaseHelper.getAllDocuments();
    ObservableList<String> documentStrings = FXCollections.observableArrayList();
    for (Document doc : documents) {
      documentStrings.add(doc.getId() + " - " + doc.getTitle() + " - " + doc.getAuthor() + " - "
          + doc.getPublicYear() + " - " + doc.getPublisher() + " - " + doc.getGenre() + " - "
          + doc.getQuantity());
    }
    documentListView.setItems(documentStrings);
  }


  @FXML
  public void onDeleteDocumentClick() {
    String selectedDocument = documentListView.getSelectionModel().getSelectedItem();
    if (selectedDocument != null) {
      int id = Integer.parseInt(
          selectedDocument.split(" - ")[0]); // Giả định format là "ID - Title"

      //Show Box
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Xóa tài liệu");
      dialog.setHeaderText("Nhập số lượng sách cần xóa:");
      dialog.setContentText("Số lượng:");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()) {
        try {
          int quantityToDelete = Integer.parseInt(result.get());

          // Gọi phương thức để xóa số lượng tài liệu tương ứng
          if (quantityToDelete < 0) {
            showWarningAlert("Vui lòng nhập một số hợp lệ.");
            return;
          }
          if (quantityToDelete == 0) {
            return;
          }
          databaseHelper.deleteDocument(id, quantityToDelete); // Gọi hàm void

          // Cập nhật danh sách tài liệu
          updateDocumentList();
        } catch (NumberFormatException e) {
          showWarningAlert("Vui lòng nhập một số hợp lệ.");
        }
      }
    } else {
      showWarningAlert("Vui lòng chọn tài liệu để xóa.");
    }
  }


  public static void showWarningAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showSuccessAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION); // Sử dụng INFORMATION
    alert.setTitle("Success");
    alert.setHeaderText("Operation Completed");
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


  @FXML
  private Button myButton;


  @FXML
  public void initializeMoving() {
    System.out.println("Controller initialized");
    myButton.setOnMouseEntered(event -> moveButton(-5));
    myButton.setOnMouseExited(event -> moveButton(0));
  }

  private void moveButton(double translateY) {
    System.out.println("Moving button to: " + translateY); // Kiểm tra giá trị translateY
    TranslateTransition transition = new TranslateTransition(Duration.millis(200), myButton);
    transition.setToY(translateY);
    transition.play();
  }

  private void changeScene(ActionEvent event, String fxmlPath, String title) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
      Scene scene = new Scene(fxmlLoader.load(), 1100, 650);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setScene(scene);
      stage.setTitle(title);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onChangeToSearchingClick(ActionEvent event) {
    changeScene(event, "/org/example/library/search-docs-view.fxml", "Tìm kiếm tài liệu");
  }

  public void giveBlackSpace() {
    titleField.clear();
    authorField.clear();
    publicYearField.clear();
    publisherField.clear();
    genreField.clear();
    quantityField.clear();
  }
}