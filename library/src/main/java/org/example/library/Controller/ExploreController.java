package org.example.library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import org.example.library.Application.DatabaseHelper;
import org.example.library.Application.Document;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ExploreController {
    @FXML
    private TextField searchExploreField;
    @FXML
    private ListView<String> suggestionsList;
    @FXML
    private ImageView myImageView;
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
    private Hyperlink myHyperlink;
    @FXML
    private String googleSearchUrl;

    private DatabaseHelper databaseHelper;

    @FXML
    public void initialize() {
        databaseHelper = new DatabaseHelper();

        // Lắng nghe sự kiện nhập liệu trong TextField
        searchExploreField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                updateSuggestions(newValue);
            } else {
                suggestionsList.getItems().clear(); // Xóa gợi ý khi không có nội dung
            }
        });

        // Lắng nghe sự kiện chọn mục trong ListView
        suggestionsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadDocumentDetails(newValue); // Gọi phương thức để nạp thông tin tài liệu
            }
        });

        //Document document = databaseHelper.getDocumentByTitle(title); // Lấy tài liệu theo tiêu đề
        //bookTitle = document.getTitle();// Hoặc lấy từ database hoặc nguồn khác
        myHyperlink.setOnAction(event -> handleHyperlinkAction());
    }

    private void updateSuggestions(String query) {
        List<String> suggestions = databaseHelper.getSuggestions(query);
        ObservableList<String> observableSuggestions = FXCollections.observableArrayList(suggestions);
        suggestionsList.setItems(observableSuggestions);
    }

    private void loadDocumentDetails(String title) {
        Document document = databaseHelper.getDocumentByTitle(title); // Lấy tài liệu theo tiêu đề

        if (document != null) {
            // Cập nhật các trường với thông tin tài liệu
            idField.setText(String.valueOf(document.getId()));
            titleField.setText(document.getTitle());
            authorField.setText(document.getAuthor());
            publicYearField.setText(String.valueOf(document.getPublicYear()));
            publisherField.setText(document.getPublisher());
            genreField.setText(document.getGenre());
            quantityField.setText(String.valueOf(document.getQuantity()));

            // Tải hình ảnh từ liên kết
            Image image = new Image(document.getImageLink(), true); // Tải hình ảnh bất đồng bộ
            myImageView.setImage(image);

            googleSearchUrl = "https://www.google.com/search?q=" + (title + " book").replace(" ", "+");

        } else {
            // Xử lý nếu không tìm thấy tài liệu
            System.out.println("Không tìm thấy tài liệu.");
        }
    }

    @FXML
    private void handleHyperlinkAction() {
        searchInGoogleByLink(googleSearchUrl);
    }
    private void searchInGoogleByLink(String googleSearchUrl) {
        try {
            //String googleSearchUrl = "https://www.google.com/search?q=" + title.replace(" ", "+");
            Desktop.getDesktop().browse(new URI(googleSearchUrl));
            System.out.println(googleSearchUrl);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}