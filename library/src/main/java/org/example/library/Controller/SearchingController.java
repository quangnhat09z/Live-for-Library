package org.example.library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.library.Application.Document;
import org.example.library.Application.searchDocument;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.example.library.Controller.HelloController.showInfoAlert;
import static org.example.library.Controller.HelloController.showWarningAlert;
import org.example.library.Application.DatabaseHelper;

public class SearchingController {
    private DatabaseHelper databaseHelper;

    public SearchingController() {
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
    public void onDeleteDocumentInSearchingClick() {
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
    private void onChangeToManageClick(ActionEvent event) {
        changeScene(event, "/org/example/library/management-view.fxml", "Quản lý tài liệu");
    }
}
