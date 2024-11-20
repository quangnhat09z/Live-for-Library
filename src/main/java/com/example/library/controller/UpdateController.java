package com.example.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;

public class UpdateController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField searchField;
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
    private TextField imageLinkField;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<String> documentListView;
    @FXML
    private Button homeButton;
    @FXML
    private Button button1;
    @FXML
    private Button exploreButton;
    @FXML
    private Button button3;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button showButton;
    @FXML
    private Button changeButton;
    @FXML
    private Button accountButton;

    private DatabaseHelper databaseHelper = new DatabaseHelper(); // Initialize DatabaseHelper
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key

    @FXML
    public void initialize() {
        homeButton.setOnAction(actionEvent -> handleHomeButton());
        button1.setOnAction(actionEvent -> handleButton1());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        button3.setOnAction(actionEvent -> handleButton3());
        addButton.setOnAction(actionEvent -> handleAddButton());
        deleteButton.setOnAction(actionEvent -> handleDeleteButton());
        showButton.setOnAction(actionEvent -> handleShowButton());
        changeButton.setOnAction(actionEvent -> handleChangeButton());
        accountButton.setOnAction(actionEvent -> handleAccountButton());
    }

    //Sẽ được cài đặt khi có view của Phước.
    private void handleAccountButton() {
//        changeScene(path, title);
    }

    private void handleHomeButton() {
        changeScene("/com/example/library/main-view.fxml", "Live for Library");
    }

    private void handleButton1() {
        // Xử lý cho nút Button1
        System.out.println("Button1 clicked");
    }

    private void handleExploreButton() {
        // Xử lý cho nút Button2
        changeScene("/com/example/library/explore-view.fxml", "Explore");
    }

    private void handleButton3() {
        // Xử lý cho nút Button3
        System.out.println("Button3 clicked");
    }

    private void handleAddButton() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String publicYear = publicYearField.getText().trim();
        String publisher = publisherField.getText().trim();
        String genre = genreField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String imageLink = imageLinkField.getText();

        if (title.isEmpty() || author.isEmpty() || publicYear.isEmpty() || publisher.isEmpty() || genre.isEmpty() || quantityText.isEmpty()) {
            showWarningAlert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            int id = 0; // Hoặc để id = -1 nếu bạn không có giá trị cụ thể
            Document document = new Document(id, title, author, publicYear, publisher, genre, quantity, imageLink);
            databaseHelper.addDocument(document); // Gọi phương thức addDocument
            updateDocumentList();
            showWarningAlert("Tài liệu đã được thêm thành công!");
        } catch (NumberFormatException e) {
            showWarningAlert("Số lượng phải là một số hợp lệ.");
        } catch (SQLException e) {
            showWarningAlert("Lỗi khi thêm tài liệu: " + e.getMessage());
        }
    }

    public void handleDeleteButton() {
        String selectedDocument = documentListView.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            int id = Integer.parseInt(selectedDocument.split(" - ")[0]); // Giả định format là "ID - Title"

            // Hộp thoại để nhập số lượng cần xóa
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

    public void handleShowButton() {
        updateDocumentList(); // Cập nhật danh sách tài liệu
    }

    private void handleChangeButton() {
        changeScene("/com/example/library/search-view.fxml", "Search Documents");
    }

    private void updateDocumentList() {
        List<Document> documents = databaseHelper.getAllDocuments();
        ObservableList<String> documentStrings = FXCollections.observableArrayList();
        for (Document doc : documents) {
            documentStrings.add(doc.getId() + " - " + doc.getTitle() + " - " + doc.getAuthor() + " - "
                    + doc.getPublicYear() + " - " + doc.getPublisher() + " - " + doc.getGenre() + " - " + doc.getQuantity());
        }
        documentListView.setItems(documentStrings);
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

    private void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) button1.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}