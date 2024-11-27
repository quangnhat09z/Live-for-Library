package com.example.library.controller.search;

import com.example.library.controller.mainScreen.Controller;
import com.example.library.model.DocumentChangeInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.example.library.model.DatabaseHelper;
import com.example.library.model.Document;
import com.example.library.model.searchDocument;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.library.model.Alert.showInfoAlert;
import static com.example.library.model.Alert.showWarningAlert;
import static com.example.library.model.SoundUtil.applySoundEffectsToButtons;


public class SearchDocumentsController extends SearchController {
    private DatabaseHelper databaseHelper;

    public SearchDocumentsController() {
        databaseHelper = new DatabaseHelper(); // Khởi tạo đối tượng DatabaseHelper
    }
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
    private Button bookButton;
    @FXML
    private Button exploreButton;
    @FXML
    private Button gameButton;
    @FXML
    private Button accountsButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button changeInfoButton;
    @FXML
    private Button manageButton;


    @Override
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
        //if (myButton != null ) initializeMoving();

        homeButton.setOnAction(actionEvent -> handleHomeButton());
        bookButton.setOnAction(actionEvent -> handleBookButton());
        exploreButton.setOnAction(actionEvent -> handleExploreButton());
        gameButton.setOnAction(actionEvent -> handleGameButton());
        searchButton.setOnAction(actionEvent -> handleSearchButton());
        deleteButton.setOnAction(actionEvent -> handleDeleteButton());
        changeInfoButton.setOnAction(actionEvent -> handleChangeInfoButton());
        manageButton.setOnAction(actionEvent -> handleChangeToManageButton());
        accountsButton.setOnAction(actionEvent -> handleChangeButton());

        if (Controller.isDarkMode()) {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/dark_search.css")).toExternalForm());

        } else {
            root.getStylesheets().clear();
            root.getStylesheets().add(Objects.requireNonNull(
                    getClass().getResource("/CSSStyling/search.css")).toExternalForm());
        }

        applySoundEffectsToButtons(root);
    }

    @Override
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
            showInfoAlert("NOTIFICATIONS", "Can't find the book you need?", "Please find another book.");
            System.out.println("No results found.");
        } else {
            System.out.println("Results found: " + results.size());
        }
        // Hiển thị kết quả tìm kiếm trong TableView
        ObservableList<Document> observableResults = FXCollections.observableArrayList(results);
        resultsTableView.setItems(observableResults);

    }

    @Override
    protected void handleDeleteButton() {
        String selectedDocument = String.valueOf(resultsTableView.getSelectionModel().getSelectedItem());

        // Kiểm tra nếu không có tài liệu nào được chọn
        if (selectedDocument == null || selectedDocument.equals("null")) {
            showWarningAlert("Please select a document to delete.");
        } else {
            int id = Integer.parseInt(selectedDocument.split(" - ")[0]); // Giả định format là "ID - Title"

            // Hộp thoại để nhập số lượng cần xóa
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete documents");
            dialog.setHeaderText("Enter the number of books to delete:");
            dialog.setContentText("Quantity:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    int quantityToDelete = Integer.parseInt(result.get());
                    // Gọi phương thức để xóa số lượng tài liệu tương ứng
                    databaseHelper.deleteDocument(id, quantityToDelete);
                } catch (NumberFormatException e) {
                    showWarningAlert("Please enter a valid number");
                }
            }
        }
    }

    @Override
    protected void handleChangeInfoButton() {
        Document selectedDocument = resultsTableView.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            // Tạo hộp thoại
            Dialog<Document> dialog = new Dialog<>();
            dialog.setTitle("Update documents");
            dialog.setHeaderText("Change document's information");

            // Tạo các trường nhập liệu
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(30, 150, 30, 30));



            TextField titleField = new TextField(selectedDocument.getTitle());
            titleField.setPrefWidth(400); // Chiều rộng ưa thích cho trường Tiêu đề

            TextField authorField = new TextField(selectedDocument.getAuthor());
            authorField.setPrefWidth(400);

            TextField publicYearField = new TextField(String.valueOf(selectedDocument.getPublicYear()));
            publicYearField.setPrefWidth(400);

            TextField publisherField = new TextField(selectedDocument.getPublisher());
            publisherField.setPrefWidth(400);

            TextField genreField = new TextField(selectedDocument.getGenre());
            genreField.setPrefWidth(400);

            TextField quantityField = new TextField(String.valueOf(selectedDocument.getQuantity()));
            quantityField.setPrefWidth(400);

            TextField imageLinkField = new TextField(selectedDocument.getImageLink());
            imageLinkField.setPrefWidth(400);

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Author:"), 0, 1);
            grid.add(authorField, 1, 1);
            grid.add(new Label("Year of publication:"), 0, 2);
            grid.add(publicYearField, 1, 2);
            grid.add(new Label("Publisher:"), 0, 3);
            grid.add(publisherField, 1, 3);
            grid.add(new Label("Genre:"), 0, 4);
            grid.add(genreField, 1, 4);
            grid.add(new Label("Quantity:"), 0, 5);
            grid.add(quantityField, 1, 5);
            grid.add(new Label("Image link:"), 0, 6);
            grid.add(imageLinkField, 1, 6);

            dialog.getDialogPane().setContent(grid);

            // Thêm nút "OK" và "Cancel"
            ButtonType okButton = new ButtonType("Cập nhật", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            // Xử lý kết quả khi nhấn nút "OK"
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Document(
                            titleField.getText(),
                            authorField.getText(),
                            publicYearField.getText(),
                            publisherField.getText(),
                            genreField.getText(),
                            Integer.parseInt(quantityField.getText()),
                            imageLinkField.getText()
                    );
                }
                return null;
            });

            // Hiển thị hộp thoại và lấy kết quả
            dialog.showAndWait().ifPresent(updatedDocument -> {
                DocumentChangeInfo documentDAO = new DocumentChangeInfo();
                boolean isUpdated = documentDAO.updateDocument(
                        selectedDocument.getId(),
                        updatedDocument.getTitle(),
                        updatedDocument.getAuthor(),
                        updatedDocument.getPublicYear(),
                        updatedDocument.getPublisher(),
                        updatedDocument.getGenre(),
                        updatedDocument.getQuantity(),
                        updatedDocument.getImageLink());

                if (isUpdated) {
                    System.out.println("Update successfully!");
                    // Cập nhật lại danh sách tài liệu trong bảng
                } else {
                    System.out.println("Update failed!");
                }
            });
        } else {
            // Thông báo nếu không có tài liệu nào được chọn
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("NOTIFICATIONS");
            alert.setContentText("Please select a document to update.");
            alert.showAndWait();
        }
    }

    @Override
    protected void handleChangeToManageButton() {
        changeScene("/com/example/library/manage-documents-view.fxml", "Manage Documents");
    }

    @Override
    protected void handleChangeButton() {
        changeScene("/com/example/library/manage-accounts-view.fxml", "Accounts");
    }

    @Override
    protected void changeScene(String fxmlPath, String title) {
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
