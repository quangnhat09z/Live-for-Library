
package com.example.library.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;
import com.example.library.model.searchDocument;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.library.model.Alert.showInfoAlert;
import static com.example.library.model.Alert.showWarningAlert;
import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;


public class BooksController extends Controller {
    private DatabaseHelper databaseHelper;

    public BooksController() {
        databaseHelper = new DatabaseHelper(); // Khởi tạo đối tượng DatabaseHelper
    }
    private BorrowController borrowController;

    @FXML
    private HBox root;
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
    private Button exploreButton;
    @FXML
    private Button gameButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button chooseBookButton;


    public void initialize() {
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

        searchButton.setOnAction(actionEvent -> handleSearchButton());
        chooseBookButton.setOnAction(actionEvent -> handleBorrowButton());

        homeButton.setOnAction(actionEvent -> handleHomeButton());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        gameButton.setOnAction(actionEvent -> handleGameButton());

        if (Controller.isDarkMode()) {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/dark_book.css")).toExternalForm());

        } else {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/book.css")).toExternalForm());
        }

        applySoundEffectsToButtons(root);
    }

    protected void handleSearchButton() {
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
    private void handleBorrowButton() {
        Document selectedDocument = resultsTableView.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/library/borrowReturn-view.fxml"));
                Parent root = loader.load();
                // Lấy controller từ FXMLLoader
                borrowController = loader.getController(); // Lưu controller vào biến
                Stage stage = (Stage) homeButton.getScene().getWindow();
                Scene scene = new Scene(root, 1300, 650);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (borrowController != null) {
                String title = selectedDocument.getTitle();
                borrowController.loadDocumentDetails(title); // Gọi phương thức với title
            } else {
                System.out.println("ExploreController chưa được khởi tạo.");
            }
        } else {
            showWarningAlert("Vui lòng chọn một cuốn sách để mượn");
        }
    }

    protected void changeScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            // Lấy controller từ FXMLLoader
            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root, 1300, 650);
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
