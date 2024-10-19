package org.example.library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import org.example.library.Application.DatabaseHelper;
import org.example.library.Application.Document;
import org.example.library.Application.searchDocument;
import org.json.JSONArray;
import org.json.JSONObject;

public class HelloController {
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

    @FXML
    private TextField findField;

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

        if (title.isEmpty() || author.isEmpty() || publicYear.isEmpty() || publisher.isEmpty() || genre.isEmpty() || quantityText.isEmpty()) {
            showWarningAlert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            int id = 0; // Hoặc để id = -1 nếu bạn không có giá trị cụ thể
            Document document = new Document(id, title, author, publicYear, publisher, genre, quantity);
            databaseHelper.addDocument(document); // Gọi phương thức addDocument
            updateDocumentList();
            showWarningAlert("Tài liệu đã được thêm thành công!");
        } catch (NumberFormatException e) {
            showWarningAlert("Số lượng phải là một số hợp lệ.");
        } catch (SQLException e) {
            showWarningAlert("Lỗi khi thêm tài liệu: " + e.getMessage());
        }
    }

    public void onShowDocumentsClick(ActionEvent actionEvent) {
        updateDocumentList(); // Cập nhật danh sách tài liệu
    }

    private void updateDocumentList() {
        List<Document> documents = databaseHelper.getAllDocuments();
        ObservableList<String> documentStrings = FXCollections.observableArrayList();
        for (Document doc : documents) {
            documentStrings.add(doc.getId() + " - " + doc.getTitle() +" - " + doc.getAuthor() + " - "
                    + doc.getPublicYear() + " - " + doc.getPublisher() + " - " + doc.getGenre() + " - " + doc.getQuantity() );
        }
        documentListView.setItems(documentStrings);
    }



    @FXML
    public void onDeleteDocumentClick() {
        String selectedDocument = documentListView.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            int id = Integer.parseInt(selectedDocument.split(" - ")[0]); // Assume format is "ID - Title"
            databaseHelper.deleteDocument(id); // Delete document from database
            updateDocumentList(); // Update document list
            showSuccessAlert("Tài liệu đã được xóa.");
        } else {
            showWarningAlert("Vui lòng chọn tài liệu để xóa.");
        }
    }
    @FXML
    public void onDeleteDocumentInSearchingClick() {
        String selectedDocument = String.valueOf(resultsListView.getSelectionModel().getSelectedItem());

        // Kiểm tra nếu không có tài liệu nào được chọn
        if (selectedDocument == null || selectedDocument.equals("null")) {
            showWarningAlert("Vui lòng chọn tài liệu để xóa.");
        } else {
            int id = Integer.parseInt(selectedDocument.split(" - ")[0]); // Giả định format là "ID - Title"
            databaseHelper.deleteDocument(id); // Xóa tài liệu khỏi database
            showSuccessAlert("Tài liệu đã được xóa.");
        }
    }





    private void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Sử dụng INFORMATION
        alert.setTitle("Success");
        alert.setHeaderText("Operation Completed");
        alert.setContentText(message);
        alert.showAndWait();
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
        changeScene(event, "/org/example/library/search-view.fxml", "Tìm kiếm tài liệu");
    }

    @FXML
    private void onChangeToManageClick(ActionEvent event) {
        changeScene(event, "/org/example/library/management-view.fxml", "Quản lý tài liệu");
    }


    @FXML
    private void onSearchClick(ActionEvent event) {
        String id = idField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String year = publicYearField.getText();
        String publisher = publisherField.getText();
        String genre = genreField.getText();
        String quantity = quantityField.getText();

        searchDocument search = new searchDocument();
        List<Document> results = search.searchBooks(id, title, author, year, publisher, genre, quantity);

        // Hiển thị kết quả tìm kiếm trong ListView
        resultsListView.getItems().clear();
        for (Document doc : results) {
            resultsListView.getItems().add(doc.toString());
        }
    }

}