package com.example.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;
import com.example.library.model.searchDocument;

import static com.example.library.controller.UpdateController.showInfoAlert;
import static com.example.library.controller.UpdateController.showWarningAlert;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class SearchController {
    private DatabaseHelper databaseHelper;

    public SearchController() {
        databaseHelper = new DatabaseHelper(); // Khởi tạo đối tượng DatabaseHelper
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
    private TableView<Document> resultsTableView;
    @FXML
    private TableColumn<Document, String> idColumn;
    @FXML
    private TableColumn<Document, String> titleColumn;
    @FXML
    private TableColumn<Document, String> authorColumn;
    @FXML
    private TableColumn<Document, String> publicYearColumn;
    @FXML
    private TableColumn<Document, String> publisherColumn;
    @FXML
    private TableColumn<Document, String> genreColumn;
    @FXML
    private TableColumn<Document, String> quantityColumn;
    @FXML
    private Button homeButton;
    @FXML
    private Button button1;
    @FXML
    private Button exploreButton;
    @FXML
    private Button button3;
    @FXML
    private Button searchButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button changeButton;


    @FXML
    private void initialize() {
        String[] a = {"idColumn", "titleColumn", "authorColumn",
                "publicYearColumn", "publisherColumn", "genreColumn", "quantityColumn"};
        int cnt = 0;
        if (idColumn != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
        }
        cnt++;

        if (titleColumn != null) {
            titleColumn.setCellValueFactory(new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
        }
        cnt++;

        if (authorColumn != null) {
            authorColumn.setCellValueFactory(new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
        }
        cnt++;

        if (publicYearColumn != null) {
            publicYearColumn.setCellValueFactory(new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
        }
        cnt++;

        if (publisherColumn != null) {
            publisherColumn.setCellValueFactory(new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
        }
        cnt++;

        if (genreColumn != null) {
            genreColumn.setCellValueFactory(new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
        }
        cnt++;

        if (quantityColumn != null) {
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>(a[cnt].substring(0, a[cnt].length() - 6)));
        }
        //if (myButton != null ) initializeMoving();

        homeButton.setOnAction(actionEvent -> handleHomeButton());
        button1.setOnAction(actionEvent -> handleButton1());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        button3.setOnAction(actionEvent -> handleButton3());
        searchButton.setOnAction(actionEvent -> handleSearchButton());
        deleteButton.setOnAction(actionEvent -> handleDeleteButton());
        changeButton.setOnAction(actionEvent -> handleChangeButton());
    }

    private void handleHomeButton() {
        changeScene("/com/example/library/main-view.fxml", "Home");
    }

    private void handleButton1() {
        System.out.println("Button1 clicked");
    }

    private void handleExploreButton() {
        changeScene("/com/example/library/explore-view.fxml", "Explore");
    }

    private void handleButton3() {
        System.out.println("Button3 clicked");
    }

    @FXML
    private void handleSearchButton() {
        String id = idField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String year = publicYearField.getText();
        String publisher = publisherField.getText();
        String genre = genreField.getText();
        String quantity = quantityField.getText();

        searchDocument search = new searchDocument();
        List<Document> results = search.searchBooks(id, title, author, year, publisher, genre, quantity);

        // Kiểm tra xem có kết quả không
        if (results.isEmpty()) {
            showInfoAlert("Thông báo", "Không tìm thấy sách bạn cần", "Vui lòng tìm sách khác");
            System.out.println("No results found.");
        } else {
            System.out.println("Results found: " + results.size());
        }
        // Hiển thị kết quả tìm kiếm trong TableView
        ObservableList<Document> observableResults = FXCollections.observableArrayList(results);
        resultsTableView.setItems(observableResults);

    }

    @FXML
    public void handleDeleteButton() {
        String selectedDocument = String.valueOf(resultsTableView.getSelectionModel().getSelectedItem());

        // Kiểm tra nếu không có tài liệu nào được chọn
        if (selectedDocument == null || selectedDocument.equals("null")) {
            showWarningAlert("Vui lòng chọn tài liệu để xóa.");
        } else {
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
                    databaseHelper.deleteDocument(id, quantityToDelete);
                } catch (NumberFormatException e) {
                    showWarningAlert("Vui lòng nhập một số hợp lệ.");
                }
            }
        }
    }

    private void handleChangeButton() {
        changeScene("/com/example/library/update-view.fxml", "Manage Documents");
    }

    private void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
