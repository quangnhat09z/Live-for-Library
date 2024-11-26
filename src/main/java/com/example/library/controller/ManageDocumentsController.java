package com.example.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;

import static com.example.library.model.Alert.showWarningAlert;
import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;

public class ManageDocumentsController extends ManageController {
    @FXML
    private HBox root;
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
    private Button bookButton;
    @FXML
    private Button exploreButton;
    @FXML
    private Button gameButton;
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

    @Override
    public void initialize() {
        homeButton.setOnAction(actionEvent -> handleHomeButton());
        bookButton.setOnAction(actionEvent -> handleBookButton());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        gameButton.setOnAction(actionEvent -> handleGameButton());
        addButton.setOnAction(actionEvent -> handleAddButton());
        deleteButton.setOnAction(actionEvent -> handleDeleteButton());
        showButton.setOnAction(actionEvent -> handleShowButton());
        changeButton.setOnAction(actionEvent -> handleSearchButton());
        accountButton.setOnAction(actionEvent -> handleChangeButton());

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

    @Override
    protected void handleAddButton() {
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

    @Override
    protected void handleDeleteButton() {
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

    @Override
    protected void handleShowButton() {
        updateDocumentList();
    }

    @Override
    protected void handleSearchButton() {
        changeScene("/com/example/library/search-documents-view.fxml", "Search Documents");
    }

    @Override
    protected void handleChangeButton() {
        changeScene("/com/example/library/manage-accounts-view.fxml", "ManageAccount");
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


    @Override
    protected void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) bookButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}