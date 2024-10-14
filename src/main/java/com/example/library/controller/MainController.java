package com.example.library.controller;

import com.example.library.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.Year;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainController {
    @FXML private TableView<Book> tableView;
    @FXML private TableColumn<Book, String> colBookId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colPublishYear;
    @FXML private TableColumn<Book, Boolean> colIsBorrowed;

    @FXML private TextField tfBookId;
    @FXML private TextField tfTitle;
    @FXML private TextField tfAuthor;
    @FXML private ComboBox<Integer> cbPublishYear;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublishYear.setCellValueFactory(new PropertyValueFactory<>("publishYear"));
        colIsBorrowed.setCellValueFactory(new PropertyValueFactory<>("isBorrowed"));

        tableView.setItems(bookList);

        // Tạo danh sách các năm từ 1900 đến năm hiện tại
        int currentYear = Year.now().getValue();
        ObservableList<Integer> years = FXCollections.observableArrayList(
            IntStream.rangeClosed(1900, currentYear)
                .boxed()
                .sorted((a, b) -> b.compareTo(a))  // Sắp xếp giảm dần
                .collect(Collectors.toList())
        );
        years.add(0, YEAR_PROMPT); // Thêm giá trị đặc biệt vào đầu danh sách
        cbPublishYear.setItems(years);
        
        // Đặt cell factory để hiển thị promptText cho giá trị đặc biệt
        cbPublishYear.setCellFactory(lv -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item == YEAR_PROMPT) {
                    setText("Năm xuất bản");
                } else {
                    setText(item.toString());
                }
            }
        });
        
        // Đặt button cell để hiển thị promptText khi không có lựa chọn
        cbPublishYear.setButtonCell(new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item == YEAR_PROMPT) {
                    setText("Năm xuất bản");
                } else {
                    setText(item.toString());
                }
            }
        });
    }

    @FXML
    private void addBook() {
        try {
            String bookId = tfBookId.getText().trim();
            String title = tfTitle.getText().trim();
            String author = tfAuthor.getText().trim();
            Integer publishYear = cbPublishYear.getValue();

            if (bookId.isEmpty() || title.isEmpty() || author.isEmpty() || 
                publishYear == null || publishYear == YEAR_PROMPT) {
                showErrorAlert("Vui lòng điền đầy đủ thông tin sách.");
                return;
            }

            if (isBookIdExists(bookId)) {
                showErrorAlert("Mã sách đã tồn tại.");
                return;
            }

            Book newBook = new Book(bookId, title, author, publishYear);
            bookList.add(newBook);
            clearForm();
            showSuccessAlert("Sách đã được thêm thành công.");
        } catch (Exception e) {
            showErrorAlert("Đã xảy ra lỗi khi thêm sách: " + e.getMessage());
        }
    }

    private boolean isBookIdExists(String bookId) {
        return bookList.stream().anyMatch(book -> book.getBookId().equals(bookId));
    }

    private void clearForm() {
        tfBookId.clear();
        tfTitle.clear();
        tfAuthor.clear();
        cbPublishYear.setValue(YEAR_PROMPT); // Đặt về giá trị đặc biệt
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccessAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private static final Integer YEAR_PROMPT = 0;
}
